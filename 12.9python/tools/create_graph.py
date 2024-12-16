import csv
import string # 导入csv文件
import pandas as pd
import py2neo # 导入py2neo库
from py2neo import Graph,Node,Relationship,NodeMatcher

#父节点创建
graph = Graph("http://localhost:7474",user= "neo4j",password = "12345",name="test.db")
graph.delete_all()
node_1=Node('父节点',name='心理学')
node_2=Node('父节点',name='心理学分类')
node_3=Node('父节点',name='心理学现象')
node_4=Node('父节点',name='人格')
node_5=Node('父节点',name='心理疾病')
node_17=Node('父节点',name='人格分类')
node_21=Node('父节点',name='气质')
node_22=Node('父节点',name='心理学事件')
node_24=Node('父节点',name='心理治疗')

graph.create(node_1)
graph.create(node_2)
graph.create(node_3)
graph.create(node_4)
graph.create(node_5)
graph.create(node_17)
graph.create(node_21)
graph.create(node_22)
graph.create(node_24)

node_1_to_node_2=Relationship(node_1,'包含_心理学分类',node_2)
node_1_to_node_3=Relationship(node_1,'包含_心理学现象',node_3)
node_1_to_node_4=Relationship(node_1,'包含_人格',node_4)
node_1_to_node_5=Relationship(node_1,'包含_心理疾病',node_5)
node_4_to_node_17=Relationship(node_4,'包含_人格分类',node_17)
node_4_to_node_21=Relationship(node_4,'包含_气质',node_21)
node_1_to_node_22=Relationship(node_1,'包含_心理学事件',node_22)
node_1_to_node_24=Relationship(node_1,'包含_心理治疗',node_24)

graph.create(node_1_to_node_2)
graph.create(node_1_to_node_3)
graph.create(node_1_to_node_4)
graph.create(node_1_to_node_5)
graph.create(node_4_to_node_17)
graph.create(node_4_to_node_21)
graph.create(node_1_to_node_22)
graph.create(node_1_to_node_24)

#心理学分类
df = pd.read_csv("C:/Users/Beiming/Desktop/爬虫/files/others/knowledge.csv")
for index,row in df.iterrows():
    node_25=Node('心理学分类',name=row['name'],description=row['description'])
    graph.create(node_25)
    graph.create(Relationship(node_2,'分类_心理学',node_25))
    #print(f"{row['name'],row['description']}")

#心理学现象
#(1)心理学效应
df = pd.read_csv('C:/Users/Beiming/Desktop/爬虫/files/others/effect_new.csv') 
node_6=Node('心理学效应',name='心理学效应')
graph.create(node_6)
node_3_to_node_6=Relationship(node_3,'包含_心理学效应',node_6)
graph.create(node_3_to_node_6)

for index,row in df.iterrows():
    node_7=Node('心理学效应',name=row['name'],description=row['description'])
    graph.create(node_7)
    graph.create(Relationship(node_6,'分类_心理学效应',node_7))
    # print(f"{row['name'],row['description']}")

#（2）情绪
df = pd.read_csv('C:/Users/Beiming/Desktop/爬虫/files/others/temper.csv') 
node_8=Node('情绪',name='情绪')
graph.create(node_8)
node_3_to_node_8=Relationship(node_3,'包含_情绪',node_8)
graph.create(node_3_to_node_8)

for index,row in df.iterrows():
    node_9=Node('情绪',name=row['name'],description=row['description'])
    graph.create(node_9)
    graph.create(Relationship(node_8,'分类_心理学效应',node_9))
    #print(f"{row['name'],row['description']}")

#心理疾病
df = pd.read_csv('C:/Users/Beiming/Desktop/爬虫/files/others/disease_new.csv') 
for index,row in df.iterrows():
    try:
        disease_name = row['name']  
        symptoms = [symptom for symptom in [row['symptom1'], row['symptom2'], row['symptom3'], row['symptom4']] if symptom]
        symptoms = [f'"{symptom}"' for symptom in symptoms if symptom == symptom] #去除nan
        causes = [cause   for cause   in [row['cause1'], row['cause2'], row['cause3'], row['cause4']] if cause  ]
        causes = [f'"{cause}"'   for cause   in causes if cause == cause  ] #去除nan
        # print(symptoms)
        # print(row['name'])
        node_10 = Node('心理疾病', name=disease_name, symptoms=symptoms,causes=causes)  
        graph.create(node_10)  
        graph.create(Relationship(node_5,'分类_心理疾病',node_10))
    except AttributeError as e:
        print(e,row['name'])

#九型人格
#(1)
df = pd.read_csv('C:/Users/Beiming/Desktop/爬虫/files/others/ninetypes.csv') 
node_11=Node('九型人格','人格',name='九型人格')
graph.create(node_11)
node_17_to_node_11=Relationship(node_17,'分类_九型人格',node_11)
graph.create(node_17_to_node_11)

for index,row in df.iterrows():
    node_14=Node('九型人格','人格',name=row['name'],self_image=row['self_image'],trap=row['trap'],defense_mechanism=row['defense_mechanism'],temper=row['temper'],time_notion=row['时间观念'],wrong_trend=row['错误趋向'],health_trend=row['健康趋向'],virtue=row['德行'])
    graph.create(node_14)
    graph.create(Relationship(node_11,'分类_九型人格',node_14))
    # print(f"{row['name'],row['description']}")

#（2）大五人格
df = pd.read_csv('C:/Users/Beiming/Desktop/爬虫/files/others/bigfive.csv') 
node_12=Node('大五人格','人格',name='大五人格')
graph.create(node_12)
node_17_to_node_12=Relationship(node_17,'分类_大五人格',node_12)
graph.create(node_17_to_node_12)

for index,row in df.iterrows():
    node_13=Node('大五人格','人格',name=row['name'],description=row['description'])
    graph.create(node_13)
    graph.create(Relationship(node_12,'分类_大五人格',node_13))
    
#16型人格
node_14=Node('16型人格','人格',name='16型人格')
graph.create(node_14)
node_17_to_node_14=Relationship(node_17,'分类_16型人格',node_14)
graph.create(node_17_to_node_14)
df = pd.read_csv('C:/Users/Beiming/Desktop/爬虫/files/others/MBTI.csv') 
for index,row in df.iterrows():
    try:
        name=row['英文名称']
        name_cn = row['中文名称']  
        traits = [trait for trait in [row['人格特征1'], row['人格特征2'], row['人格特征3']] if trait]
        traits = [f'"{trait}"' for trait in traits if trait == trait] #去除nan
        advantages = [advantage   for advantage   in [row['优点1'], row['优点2'], row['优点3'], row['优点4'],row['优点5'],row['优点6']] if advantage  ]
        advantages = [f'"{advantage}"'   for advantage   in advantages if advantage == advantage  ] #去除nan
        disadvantages = [disadvantage   for disadvantage   in [row['缺点1'], row['缺点2'], row['缺点3'], row['缺点4'],row['缺点5'],row['缺点6'],row['缺点7']] if disadvantage  ]
        disadvantages = [f'"{disadvantage}"'   for disadvantage   in disadvantages if disadvantage == disadvantage  ] #去除nan
        romantic=row['恋爱']
        friendship=row['友谊']
        family=row['亲子']
        node_16 = Node(
            '16型人格','人格',
            name=name,
            name_cm=name_cn,
            traits=traits, 
            advantages=advantages, 
            disadvantages=disadvantages,
            romantic=romantic,
            family=family
            )  
        graph.create(node_16)  
        graph.create(Relationship(node_14,'分类_16型人格',node_16))
    except AttributeError as e:
        print(e,row['name'])

#人格->气质
df = pd.read_csv('C:/Users/Beiming/Desktop/爬虫/files/others/气质.csv')
for index,row in df.iterrows():
    node_18=Node('气质',name=row['name'],description=row['description'])
    graph.create(node_18)
    graph.create(Relationship(node_21,'分类_气质',node_18))
    #print(f"{row['name'],row['description']}")

query = """  
MATCH (t:情绪), (p:九型人格 {temper: t.name})  
CREATE (p)-[:偏激情绪]->(t)  
RETURN t, p
"""  
results = graph.run(query)
#print(results)

#导入三元组文件
query = """  
LOAD CSV WITH HEADERS FROM 'file:///三元组.csv' AS line  
WITH line WHERE line.`实体1` IS NOT NULL AND line.`实体2` IS NOT NULL
MERGE (p:人格 {name: line.实体1})  
MERGE (e:情绪 {name: line.实体2})  
MERGE (p)-[:Triggers{relationship_type: line.`关系`}]->(e);
"""  
results = graph.run(query)

#心理事件
df = pd.read_csv('C:/Users/Beiming/Desktop/爬虫/files/others/cleaned_cases3.csv')
for index, row in df.iterrows():
    try:
        person = row['当事人']
        feature = row['心理特征']
        role = row['事件中角色']
        temper = row['没掌控好的情绪']
        temper2 = temper.split('、')  # 按'、'拆分情绪

        process = row['事件经过']
        disease = row['心理疾病']

        node_23 = Node('心理事件', name=process, person=person, feature=feature)
        graph.create(node_23)

        # 创建心理事件和分类_心理事件的关系
        graph.create(Relationship(node_22, '分类_心理事件', node_23))

        try:
            # 查找心理疾病节点
            disease_node = graph.nodes.match("心理疾病", name=f"{disease}").first()
            if disease_node:
                relation1 = Relationship(node_23, "包含_心理疾病", disease_node)
                graph.create(relation1)
            else:
                print(f"未找到心理疾病节点: {disease}")

            # 遍历情绪列表，查找情绪节点并创建关系
            for temp in temper2:
                temp_node = graph.nodes.match("情绪", name=f"{temp}").first()
                if temp_node:
                    relation2 = Relationship(node_23, "包含_情绪", temp_node)
                    graph.create(relation2)
                else:
                    print(f"未找到情绪节点: {temp}")

        except Exception as e:
            print(f"处理心理疾病或情绪关系时出错: {e}")

    except Exception as e:
        print(f"处理心理事件 {row['事件经过']} 时出错: {e}")


df = pd.read_csv('C:/Users/Beiming/Desktop/爬虫/files/others/output_triples_cleaned.csv', encoding='gbk')

def create_entity_and_relationship(row):
    try:
        # 查询是否存在实体1
        query_entity1 = f"MATCH (n) WHERE n.name = '{row['实体1']}' RETURN n LIMIT 1"
        result_entity1 = graph.run(query_entity1).data()

        # 如果实体1不存在，创建实体1
        if not result_entity1:
            query_create_entity1 = f"CREATE (n:Entity {{name: '{row['实体1']}'}})"
            graph.run(query_create_entity1)
            print(f"实体 '{row['实体1']}' 创建成功。")

        # 查询是否存在实体2
        query_entity2 = f"MATCH (n) WHERE n.name = '{row['实体2']}' RETURN n LIMIT 1"
        result_entity2 = graph.run(query_entity2).data()

        # 如果实体2不存在，创建实体2
        if not result_entity2:
            query_create_entity2 = f"CREATE (n:Entity {{name: '{row['实体2']}'}})"
            graph.run(query_create_entity2)
            print(f"实体 '{row['实体2']}' 创建成功。")

        # 创建实体1和实体2之间的关系
        query_create_relationship = f"""
        MATCH (a {{name: '{row['实体1']}'}}), (b {{name: '{row['实体2']}'}})
        MERGE (a)-[r:{row['关系']}] -> (b)
        """

        graph.run(query_create_relationship)
        print(f"关系 '{row['关系']}' 在实体 '{row['实体1']}' 和实体 '{row['实体2']}' 之间创建成功。")

    except Exception as e:
        print(f"发生错误: {e}")

# 遍历 CSV 文件中的每一行，并调用函数创建实体和关系
for index, row in df.iterrows():
    create_entity_and_relationship(row)
