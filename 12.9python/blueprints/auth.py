# auth.py
from flask import Blueprint, jsonify, request
from exts import db
from models import User

# 设置唯一的名称，如 'auth_bp'，并将前缀改为 /auth 以避免冲突
bp = Blueprint("auth_bp", __name__, url_prefix="/auth")

# class User(db.Model):
#     __tablename__ = "user"
#     id = db.Column(db.Integer, primary_key=True, autoincrement=True)
#     username = db.Column(db.String(100), nullable=False)
#     password = db.Column(db.String(100), nullable=False)

@bp.route('/register', methods=['POST'])
def register():
    data = request.get_json()
    ad = request.remote_addr
    print("客户端地址是：" + ad)
    if not data:
        return jsonify({"message": "没有收到任何数据"}), 400
    print(data)
    un = data.get('username')
    pwd = data.get('password')
    print(f"用户名: {un}, 密码: {pwd}")

    user = User(username=un, password=pwd)
    db.session.add(user)
    db.session.commit()

    return jsonify({"message": "注册成功"}),200


@bp.route('/login', methods=['POST'])
def login():
    
    data = request.get_json()
    user = data.get('username')
    pwd = data.get('password')

    existing_user = User.query.filter_by(username=user, password=pwd).first()
    if existing_user:
        return jsonify({"message": "登录成功","user_id":existing_user.id,"scl_90":existing_user.scl_90,"SDS":existing_user.SDS}),200
    else:
        return jsonify({"message": "用户名或密码错误"}), 409
    
@bp.route('/change_username', methods=['POST'])
def change_username():
    data = request.get_json()
    if not data:
        return jsonify({"message": "没有收到任何数据"}), 400 

    user_id = data.get('id')
    new_username = data.get('username')

    if not user_id or not new_username:
        return jsonify({"message": "缺少必要参数"}), 400

    # 查找数据库中是否存在该用户
    user = User.query.get(user_id)
    if not user:
        return jsonify({"message": "用户不存在"}), 404

    # 更新用户名
    user.username = new_username
    try:
        db.session.commit()
        return jsonify({"message": "用户名修改成功"}), 200
    except Exception as e:
        db.session.rollback()  # 回滚事务以防止数据损坏
        return jsonify({"message": "用户名修改失败", "error": str(e)}), 500

@bp.route('/test')
def test():
    return "覃启航笨比"
