from flask import Flask, request, jsonify

app = Flask(__name__)

@app.route('/login', methods=['POST'])
def login():
    data = request.get_json()
    print(request.method)
    if not data:
        return jsonify({"message": "没有收到任何数据"}), 400

    username = data.get('username')
    password = data.get('password')

    print(f"用户名: {username}, 密码: {password}")

    # 处理用户名和密码（比如验证）
    if username == 'aa' and password == 'bb':
        return jsonify({"message": "登录成功"})
    else:
        return jsonify({"message": "用户名或密码错误"}), 401

    #return "Hello World!"



if __name__ == '__main__':
    print('aasdasdasdasdasdasd')
    # app.run(debug=True, host = '10.138.197.196',port=6671)
    app.run(debug=True,port=6671)
