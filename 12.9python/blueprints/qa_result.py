from flask import Blueprint, jsonify, request
from exts import db
from models import User

bp = Blueprint("qa_result_bp", __name__, url_prefix="/qa_result")

dic = {}

# 计算函数
def sql_90(score):
    if score < 40:
        return '经过检测您【无抑郁】，希望您继续保持'
    elif score < 47:
        return '经过检测您为【轻微至轻度抑郁】，为了您和家人的幸福，建议您联系我们，进行更专业的检测。'
    elif score < 55:
        return '经过检测您为【中至重度抑郁】，为了您和家人的幸福，建议您联系我们，进行更专业的检测。'
    else:
        return '经过检测您为【重度抑郁】，为了您和家人的幸福，建议您联系我们，进行更专业的检测。'

def SDS(score):
    if score < 53:
        return '正常'
    elif score < 63:
        return '轻度抑郁'
    elif score < 73:
        return '中度抑郁'
    else:
        return '重度抑郁'

dic['sql-90'] = sql_90
dic['SDS'] = SDS

@bp.route('/get_result', methods=['POST'])
def f():
    data = request.get_json()
    q = data.get('questionnairename')
    s = data.get('score')
    user_id = data.get('id')

    # 验证问卷名称
    if q not in dic:
        return jsonify({"message": "问卷名称无效"}), 400

    # 计算结果
    res = str(dic[q](s))

    # 查询用户
    user_to_update = db.session.query(User).filter_by(id=user_id).first()
    if not user_to_update:
        return jsonify({"message": "用户不存在"}), 400

    # 更新用户数据
    setattr(user_to_update, q, res)
    try:
        db.session.commit()
        return jsonify({"message": "成功获取结果", "result": res})
    except Exception as e:
        db.session.rollback()
        return jsonify({"message": "数据库更新失败", "error": str(e)}), 500
    
    # return jsonify({"message": "成功获取结果"})
