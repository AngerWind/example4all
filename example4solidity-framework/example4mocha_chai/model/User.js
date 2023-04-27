const mongoose = require("mongoose")

// 定义model
var userSchema = new mongoose.Schema({
    name: String,
    email: String,
    age: Number,
    date: {
        type: Date,
        default: Date.now
    }
});   

module.exports = mongoose.model("User", userSchema);