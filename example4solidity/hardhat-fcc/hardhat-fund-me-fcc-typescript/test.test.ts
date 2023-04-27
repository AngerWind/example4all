type Layout = {
    storage: Array<{
        astId: number;
        contract: string;
        label: string;
        offset: number;
        slot: string;
        type: string;
    }>;
    types: {
        [key: string]: {
            encoding: string;
            label: string;
            numberOfBytes: string;
            base?: string;
			members?: any;
			key?: any;
			value?: any;
        };

    };
}
const obj: Layout = {
    "storage": [
        {
            "astId": 18,
            "contract": "storage.sol:FunWithStorage",
            "label": "u8",
            "offset": 0,
            "slot": "0",
            "type": "t_uint8"
        },
        {
            "astId": 21,
            "contract": "storage.sol:FunWithStorage",
            "label": "u16",
            "offset": 1,
            "slot": "0",
            "type": "t_uint16"
        },
        {
            "astId": 24,
            "contract": "storage.sol:FunWithStorage",
            "label": "u24",
            "offset": 3,
            "slot": "0",
            "type": "t_uint24"
        },
        {
            "astId": 27,
            "contract": "storage.sol:FunWithStorage",
            "label": "u256",
            "offset": 0,
            "slot": "1",
            "type": "t_uint256"
        },
        {
            "astId": 30,
            "contract": "storage.sol:FunWithStorage",
            "label": "i8",
            "offset": 0,
            "slot": "2",
            "type": "t_int8"
        },
        {
            "astId": 33,
            "contract": "storage.sol:FunWithStorage",
            "label": "i16",
            "offset": 1,
            "slot": "2",
            "type": "t_int16"
        },
        {
            "astId": 36,
            "contract": "storage.sol:FunWithStorage",
            "label": "i24",
            "offset": 3,
            "slot": "2",
            "type": "t_int24"
        },
        {
            "astId": 39,
            "contract": "storage.sol:FunWithStorage",
            "label": "i256",
            "offset": 0,
            "slot": "3",
            "type": "t_int256"
        },
        {
            "astId": 42,
            "contract": "storage.sol:FunWithStorage",
            "label": "b",
            "offset": 0,
            "slot": "4",
            "type": "t_bool"
        },
        {
            "astId": 45,
            "contract": "storage.sol:FunWithStorage",
            "label": "addr",
            "offset": 1,
            "slot": "4",
            "type": "t_address"
        },
        {
            "astId": 48,
            "contract": "storage.sol:FunWithStorage",
            "label": "b1",
            "offset": 21,
            "slot": "4",
            "type": "t_bytes1"
        },
        {
            "astId": 51,
            "contract": "storage.sol:FunWithStorage",
            "label": "b2",
            "offset": 0,
            "slot": "5",
            "type": "t_bytes31"
        },
        {
            "astId": 54,
            "contract": "storage.sol:FunWithStorage",
            "label": "b3",
            "offset": 0,
            "slot": "6",
            "type": "t_bytes32"
        },
        {
            "astId": 59,
            "contract": "storage.sol:FunWithStorage",
            "label": "status",
            "offset": 0,
            "slot": "7",
            "type": "t_enum(Status)15"
        },
        {
            "astId": 67,
            "contract": "storage.sol:FunWithStorage",
            "label": "pair",
            "offset": 0,
            "slot": "8",
            "type": "t_struct(Pair)8_storage"
        },
        {
            "astId": 76,
            "contract": "storage.sol:FunWithStorage",
            "label": "uArray",
            "offset": 0,
            "slot": "10",
            "type": "t_array(t_uint256)4_storage"
        },
        {
            "astId": 84,
            "contract": "storage.sol:FunWithStorage",
            "label": "u128Array",
            "offset": 0,
            "slot": "14",
            "type": "t_array(t_uint128)3_storage"
        },
        {
            "astId": 92,
            "contract": "storage.sol:FunWithStorage",
            "label": "uDynamicArray",
            "offset": 0,
            "slot": "16",
            "type": "t_array(t_uint256)dyn_storage"
        },
        {
            "astId": 95,
            "contract": "storage.sol:FunWithStorage",
            "label": "s",
            "offset": 0,
            "slot": "17",
            "type": "t_string_storage"
        },
        {
            "astId": 98,
            "contract": "storage.sol:FunWithStorage",
            "label": "bs",
            "offset": 0,
            "slot": "18",
            "type": "t_bytes_storage"
        },
        {
            "astId": 101,
            "contract": "storage.sol:FunWithStorage",
            "label": "s1",
            "offset": 0,
            "slot": "19",
            "type": "t_string_storage"
        },
        {
            "astId": 105,
            "contract": "storage.sol:FunWithStorage",
            "label": "map",
            "offset": 0,
            "slot": "20",
            "type": "t_mapping(t_address,t_uint256)"
        }
    ],
    "types": {
        "t_address": {
            "encoding": "inplace",
            "label": "address",
            "numberOfBytes": "20"
        },
        "t_array(t_uint128)3_storage": {
            "base": "t_uint128",
            "encoding": "inplace",
            "label": "uint128[3]",
            "numberOfBytes": "64"
        },
        "t_array(t_uint256)4_storage": {
            "base": "t_uint256",
            "encoding": "inplace",
            "label": "uint256[4]",
            "numberOfBytes": "128"
        },
        "t_array(t_uint256)dyn_storage": {
            "base": "t_uint256",
            "encoding": "dynamic_array",
            "label": "uint256[]",
            "numberOfBytes": "32"
        },
        "t_bool": {
            "encoding": "inplace",
            "label": "bool",
            "numberOfBytes": "1"
        },
        "t_bytes1": {
            "encoding": "inplace",
            "label": "bytes1",
            "numberOfBytes": "1"
        },
        "t_bytes31": {
            "encoding": "inplace",
            "label": "bytes31",
            "numberOfBytes": "31"
        },
        "t_bytes32": {
            "encoding": "inplace",
            "label": "bytes32",
            "numberOfBytes": "32"
        },
        "t_bytes_storage": {
            "encoding": "bytes",
            "label": "bytes",
            "numberOfBytes": "32"
        },
        "t_enum(Status)15": {
            "encoding": "inplace",
            "label": "enum FunWithStorage.Status",
            "numberOfBytes": "1"
        },
        "t_int16": {
            "encoding": "inplace",
            "label": "int16",
            "numberOfBytes": "2"
        },
        "t_int24": {
            "encoding": "inplace",
            "label": "int24",
            "numberOfBytes": "3"
        },
        "t_int256": {
            "encoding": "inplace",
            "label": "int256",
            "numberOfBytes": "32"
        },
        "t_int8": {
            "encoding": "inplace",
            "label": "int8",
            "numberOfBytes": "1"
        },
        "t_mapping(t_address,t_uint256)": {
            "encoding": "mapping",
            "key": "t_address",
            "label": "mapping(address => uint256)",
            "numberOfBytes": "32",
            "value": "t_uint256"
        },
        "t_string_storage": {
            "encoding": "bytes",
            "label": "string",
            "numberOfBytes": "32"
        },
        "t_struct(Pair)8_storage": {
            "encoding": "inplace",
            "label": "struct FunWithStorage.Pair",
            "members": [
                {
                    "astId": 3,
                    "contract": "storage.sol:FunWithStorage",
                    "label": "left",
                    "offset": 0,
                    "slot": "0",
                    "type": "t_uint8"
                },
                {
                    "astId": 5,
                    "contract": "storage.sol:FunWithStorage",
                    "label": "middle",
                    "offset": 1,
                    "slot": "0",
                    "type": "t_uint8"
                },
                {
                    "astId": 7,
                    "contract": "storage.sol:FunWithStorage",
                    "label": "right",
                    "offset": 0,
                    "slot": "1",
                    "type": "t_uint256"
                }
            ],
            "numberOfBytes": "64"
        },
        "t_uint128": {
            "encoding": "inplace",
            "label": "uint128",
            "numberOfBytes": "16"
        },
        "t_uint16": {
            "encoding": "inplace",
            "label": "uint16",
            "numberOfBytes": "2"
        },
        "t_uint24": {
            "encoding": "inplace",
            "label": "uint24",
            "numberOfBytes": "3"
        },
        "t_uint256": {
            "encoding": "inplace",
            "label": "uint256",
            "numberOfBytes": "32"
        },
        "t_uint8": {
            "encoding": "inplace",
            "label": "uint8",
            "numberOfBytes": "1"
        }
    }
}

obj.storage.forEach(function (item, index) {
	console.log(`// label: ${item.label}, slot: ${item.slot}, offset: ${item.offset}`);
});

