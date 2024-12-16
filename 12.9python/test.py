import csv
import string
from fuzzywuzzy import fuzz  # 导入fuzz模块来计算字符串相似度
import pandas as pd
import py2neo  # 导入py2neo库
from py2neo import Graph, Node, Relationship, NodeMatcher
from gptapi import get
import Search  # 你需要确保 Search 是一个可以导入的模块，包含 `find_entity`, `get_entity_properties` 等函数

# Neo4j图数据库连接配置
graph = Graph("http://localhost:7474", user="neo4j", password="12345", name="test.db")

def extract_and_find_entities(text):
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
        similar_entities = Search.find_entity(entity)
        
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

# 使用示例
str1 = "考试失利又怎样"
final_result = extract_and_find_entities(str1)
print(f"最终的实体和属性字符串: {final_result}")
