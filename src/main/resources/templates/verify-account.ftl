<!DOCTYPE html>
<html lang="vi">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mã Đăng Nhập</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
        }

        .container {
            text-align: center;
            background-color: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            max-width: 400px;
            width: 100%;
        }

        img {
            width: 80px;
            height: auto;
            margin-bottom: 20px;
        }

        h1 {
            font-size: 24px;
            margin-bottom: 20px;
            color: #333;
        }

        .code-box {
            background-color: #f9f9f9;
            padding: 20px;
            border-radius: 8px;
            font-size: 36px;
            letter-spacing: 10px;
            color: #333;
            margin-bottom: 20px;
        }

        p {
            color: #666;
            font-size: 14px;
        }

        p a {
            color: #d9534f;
            text-decoration: none;
        }

        p a:hover {
            text-decoration: underline;
        }
    </style>
</head>

<body>
<div style="margin-left: 500px" class="container">
    <img src="logo.png" alt="Logo"> <!-- Thêm logo ở đây -->
    <h1>Verification Code</h1>
    <div class="code-box">${code}</div> <!-- Biến 'code' được truyền từ Controller -->
    <p>Code only lasts for 5 minutes</p>
</div>
</body>

</html>
