require("dotenv").config();
const mongoose = require("mongoose")


mongoose.connect(process.env.MONGO_URL, {
    useNewUrlParser: true,
    useUnifiedTopology: true,
    user: process.env.MONGO_USER,
    pass: process.env.MONGO_PASS,
    dbName: process.env.MONGO_DB
}).then(() => {
    console.log("MongoDB Connected");
}).catch(err => { 
    console.log(err);
})