const router = require('express').Router();
const userController = require('../controller/user'); // 获取controller, 类似于java中的service


router.post('', userController.createUser); // 定义路由和处理的controller
router.get('', userController.queryAll);
router.get('/:id', userController.queryById);
router.delete('/:id', userController.deleteUser);
router.put('/:id', userController.updateUser);

module.exports = {
    router,
    path: "/user",
}