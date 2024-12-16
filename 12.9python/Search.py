import csv
import string  # 导入csv文件
from fuzzywuzzy import fuzz  # 导入fuzz模块来计算字符串相似度
import pandas as pd
import py2neo  # 导入py2neo库
from py2neo import Graph, Node, Relationship, NodeMatcher
from gptapi import get



def find_entity(entity_name):
    graph = Graph("http://localhost:7474", user="neo4j", password="12345", name="test.db")
    # 假设你要查找与"心情"最相似的实体
    # entity_name = "考试"  # 目标实体
    threshold = 10  # 设置相似度阈值，表示找出相似度大于该值的实体

    # 查询所有实体
    query = "MATCH (n) RETURN n"
    result = graph.run(query)

    # 计算每个实体与目标实体的相似度
    similar_entities = []
    for record in result:
        entity = record["n"]
        entity_name_in_graph = entity.get("name", "")  # 获取实体名称
        similarity = fuzz.ratio(entity_name, entity_name_in_graph)  # 计算相似度
        
        if similarity >= threshold:  # 判断相似度是否超过阈值
            similar_entities.append((entity_name_in_graph, similarity))

    # 排序并输出最相似的实体
    similar_entities = sorted(similar_entities, key=lambda x: x[1], reverse=True)[:3]

    # 根据匹配数量进行显示并获取邻近节点
    if len(similar_entities) == 0:
        print("没有匹配到实体")  # 没有匹配到实体
    elif len(similar_entities) == 1:
        entity, similarity = similar_entities[0]
        print(f"只有一个匹配实体: 实体: {entity}, 相似度: {similarity}")
        # 获取该实体的邻近节点
        try:
            query_neighbors = f"MATCH (n:{entity})-[r]->(m) RETURN m, r LIMIT 3"
            result_neighbors = graph.run(query_neighbors)
            print("邻近节点：")
            # for record in result_neighbors:
            #     neighbor = record["m"]
            #     relation = record["r"]
            #     print(f"邻近实体: {neighbor['name']}, 关系: {relation}")
        except Exception as e:
            print(f"发生错误: {e}")
    elif len(similar_entities) >= 2:
        # 选择前两个最相似的实体
        # print("最相似的两个实体：")
        try:
            for entity, similarity in similar_entities[:2]:
                # print(f"实体: {entity}, 相似度: {similarity}")
                # 获取该实体的邻近节点
                query_neighbors = f"MATCH (n:{entity})-[r]->(m) RETURN m, r LIMIT 3"
                result_neighbors = graph.run(query_neighbors)
                #print("邻近节点：")
                for record in result_neighbors:
                    neighbor = record["m"]
                    relation = record["r"]
                    #print(f"邻近实体: {neighbor['name']}, 关系: {relation}")
                
        except Exception as e:
            print(f"发生错误: {e}")

    similar_entity_names = [entity for entity, similarity in similar_entities]
    # print(similar_entity_names)
    return similar_entity_names


def find_neighbors(entity_name):
    graph = Graph("http://localhost:7474", user="neo4j", password="12345", name="test.db")
    try:
        # 查询指定实体的所有属性
        query_properties = f"MATCH (n) WHERE n.name = '{entity_name}' RETURN n LIMIT 1"
        
        # 查询入度邻接实体
        query_in_neighbors = f"MATCH (n)<-[r]-(m) WHERE n.name = '{entity_name}' RETURN m, r LIMIT 3"
        result_in_neighbors = graph.run(query_in_neighbors)

        in_neighbors = []
        for record in result_in_neighbors:
            neighbor = record["m"]
            neighbor_name = neighbor.get("name", "未知")  # 获取邻近实体的名称

            in_neighbors.append(neighbor_name)

        # 查询出度邻接实体
        query_out_neighbors = f"MATCH (n)-[r]->(m) WHERE n.name = '{entity_name}' RETURN m, r LIMIT 3"
        result_out_neighbors = graph.run(query_out_neighbors)

        out_neighbors = []
        for record in result_out_neighbors:
            neighbor = record["m"]
            neighbor_name = neighbor.get("name", "未知")  # 获取邻近实体的名称

            out_neighbors.append(neighbor_name)

        # 合并入度和出度邻接实体并限制数量为 3
        neighbors = in_neighbors + out_neighbors
        neighbors = [n for n in neighbors if isinstance(n[1], dict) and n[1].get("name") != "父节点"]
        neighbors = neighbors[:3]  # 限制为最多3个邻接实体

        return neighbors

    except Exception as e:
        print(f"发生错误: {e}")
        return []

def get_entity_properties(entity_name):
    graph = Graph("http://localhost:7474",user="neo4j",password="12345", name="test.db")
    try:
        # 查询指定实体的所有属性
        query_properties = f"MATCH (n) WHERE n.name = '{entity_name}' RETURN n LIMIT 1"
        result_properties = graph.run(query_properties)

        # 获取实体的属性
        entity_properties = []
        for record in result_properties:
            entity = record["n"]
            # 获取实体的所有属性并添加到列表中
            entity_properties.append(dict(entity))  # 将节点的属性转化为字典并添加到列表
    
        # print(entity_properties)
        return entity_properties

    except Exception as e:
        print(f"发生错误: {e}")
        return {}
    
def extract_and_find_entities(text):
    graph = Graph("http://localhost:7474", user="neo4j", password="12345", name="test.db")
    """
    提取文本中的实体并在 Neo4j 图数据库中查找相似的实体及其属性。

    参数:
    text (str): 要处理的文本。

    返回:
    str: 最终的实体和属性字符串。
    """
    # 提取文本中的实体
    prompt = f"""提取以下句子中的所有的实体：{text},只输出提取结果,相邻实体用英文逗号隔开。不要扔任何多余的回答。

    【输出格式】
    ['实体1','实体2','实体3'],是一个list

    举例1:输入:今天考试失利了  输出：考试,失利

    举例2:输入:我被妈妈骂了一早上    输出：妈妈,骂
    """
    
    # 提取文本中的实体
    result = get(prompt)

    # 去除多余的空白字符和换行符
    result = result.replace("\n", "").replace(" ", "")

    # 如果返回的 result 不是合法的 list，尝试用 eval 转换为 list
    try:
        entities = eval(result)
    except:
        print("Error: Could not parse the result into a valid list.")
        return ""

    # 存储所有实体的名称和属性
    all_entities_and_properties = []

    # 遍历实体并查找相似实体和属性
    for entity in entities:
        # 查找相似的实体
        similar_entities = find_entity(entity)
        
        # 对相似实体中的每个元素进行清洗，只保留字符串部分
        cleaned_similar_entities = []
        for item in similar_entities:
            # 只有在类型检查中明确确保 item 是有效的类型
            if isinstance(item, str):
                cleaned_similar_entities.append(item)
        
        # 合并清洗后的实体
        all_entities_and_properties.extend(cleaned_similar_entities)

    # 将所有实体和属性转换为字符串并连接成一个字符串
    def safe_str(item):
        # 直接转换为字符串
        return str(item)

    # 确保只连接字符串类型的元素
    result_string = ", ".join(map(safe_str, all_entities_and_properties))
    return result_string

# similar_entities=find_entity("自杀")
# if similar_entities:
#     # 使用第一个最相似的实体查找邻近实体
#     print("开始获取实体属性")
#     get_entity_properties(similar_entities[0])

# else:
#     print("没有相似实体")
#     print("开始寻找相邻实体")
#     first_entity = similar_entities[0]  # 获取第一个实体的名称
#     # print(first_entity)
#     neighbors = find_neighbors(first_entity)
#     print(f"{first_entity} 的邻近实体: {neighbors}")


# get_entity_properties("嗜睡症")


#####先属性，后邻接结点
