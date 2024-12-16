from flask import Blueprint, jsonify, request
from exts import db
from gptapi import get
import Search
# 设置唯一的名称，如 'auth_bp'，并将前缀改为 /auth 以避免冲突
bp = Blueprint("chat_bp", __name__, url_prefix="/chat")

   

@bp.route('/formal_chat', methods=['POST'])
def formal_chat():
    data = request.get_json()
    ct = data.get('content')
    print(f"内容: {ct}")
    if(len(ct)<=3):
        ans=get(f"""
        我现在做心理咨询，请你像心理医生一样，结合用户对话进行给出积极的例子，进行心理疏导，回答字数不要太长
        用户输入：{ct}
        
        """)
    else:
        better=Search.extract_and_find_entities(ct)
        if(better=="没有匹配到实体"):
            ans=get(ct)
        else:
            ans=get(f"""用户的输入为{ct}，适当根据用户的心理状态关键词{better}，回答总字数不超50个字。用户正在进行心理咨询，请你像心理医生一样，结合用户的对话给出积极的例子，不需要指定输入和输出，正常回答即可。根据用户输入的心理知识图谱中的实体关键字给出回答,并适当给出建议。
示例：
    用户输入：我心情不好
    输出：你可以先放松一下，然后休息的时候找一些问题让自己开心，比如听音乐、看电影。
    用户输入：有些生气
    输出：找些开心的事情做做，比如听音乐、看电影。
                """)

    
    print(ans)
    print(jsonify({"content": ans}))
    return jsonify({"content": ans}),200

@bp.route('/test',methods=['GET'])
def test():
    return "世界你好"