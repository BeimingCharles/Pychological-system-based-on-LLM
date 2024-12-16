from flask import Blueprint, jsonify, request
from exts import db
from models import scl_90,SDS,questionnaire_index

bp = Blueprint("qa_bp", __name__, url_prefix="/qa")

@bp.route('/get_qa',methods = ['POST'])
def get_questionnaire():
    data = request.get_json()
    name = data.get('questionnaire_name')
    #print(name)
    questionnaire = questionnaire_index.query.filter_by(questionnaire_name = name).first()
    #print(questionnaire)
    if not questionnaire:
        return jsonify({"message":"问卷不存在"}),606
    table_name = questionnaire.q_table_name
    #print(table_name)
    questionnaire_need = globals().get(table_name)
    if not questionnaire_need:
        return jsonify({"message": "问卷不存在"}), 688
    questions = questionnaire_need.query.all()
    #print(result)
    result = [{"id":q.id,"question":q.question} for q in questions]
    return jsonify({"answer":questionnaire.questionnaire_num,"questions":result})

@bp.route('/get_all_qa', methods=['GET'])
def get_qa_name():
    try:
        print("hello")
        rows = questionnaire_index.query.all()
        print(rows)
        print(rows[0].questionnaire_name)
        if not rows:
            return jsonify({"error": "No questionnaires found"}), 404
        
        questionnaire_names = [i.questionnaire_name for i in rows]
        questionnaire_introductions = [i.questionnaire_introduction for i in rows]
        print(questionnaire_names)
        
        return jsonify({
            "names": questionnaire_names,
            "introductions": questionnaire_introductions
        })
    except Exception as e:
        # 这里可以记录日志
        print("错误是"+str(e))
        return jsonify({"error": "An error occurred while fetching data."}), 500


    