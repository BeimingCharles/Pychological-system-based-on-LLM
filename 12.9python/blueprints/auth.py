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

@bp.route('/test')
def test():
    return "覃启航笨比"
