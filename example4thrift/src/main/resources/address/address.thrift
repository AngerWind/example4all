namespace java com.tiger.example.address

struct Address {
    1: required string city // 城市(必须)
    2: required string province // 省份(必须)
    3: optional i64 postal // 邮政编码(可选)
}