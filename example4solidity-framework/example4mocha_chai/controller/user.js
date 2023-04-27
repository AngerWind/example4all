const User = require("../model/User");

exports.createUser = async function (req, res) {
  if (!req.body.name) {
    return res.status(422).send({
      message: "User name can not be empty",
    });
  }
  if (!req.body.email) {
    return res.status(422).send({
      message: "User email can not be empty",
    });
  }
  if (!req.body.age) {
    return res.status(422).send({
      message: "User age can not be empty",
    });
  }

  var user = new User(req.body); // 直接从request body中获取数据并创建一个user
  // user.save(function (err, user) {
  //     if (err) {
  //         return res.status(400).send({
  //             message: err
  //         });
  //     } else {
  //         res.json(user);
  //     }
  // });
  try {
    // const newUser = await user.save();
    // res.status(201).json(newUser);

    const result = await User.create(user);
    res.status(201).json(result);
  } catch (err) {
    res.status(500).json({ message: err.message });
  }
};

exports.queryAll = async function (req, res) {
  try {
    const users = await User.find().sort({ date: "desc" });
    res.json(users);
  } catch (err) {
    res.status(500).json({ message: err.message });
  }
};

exports.queryById = async function (req, res) {
  try {
    const user = await User.findById(req.params.id);
    res.json(user);
  } catch (err) {
    res.status(500).json({ message: err.message });
  }
};

exports.deleteUser = async function (req, res) {
  try {
    const user = await User.findByIdAndDelete(req.params.id);
    res.json(user);
  } catch (err) {
    res.status(500).json({ message: err.message });
  }
};

exports.updateUser = async function (req, res) {
  try {
    const { name, email, age } = req.body;
    const user = await User.findByIdAndUpdate(
      { _id: req.params.id }, // 根据id查找
      { $set: { name, email, age } }, // 更新的字段
      { new: false }
    );
    res.json(user);
  } catch (error) {
    res.status(500).json({ message: err.message });
  }
};
