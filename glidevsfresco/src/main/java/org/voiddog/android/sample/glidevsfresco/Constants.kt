package org.voiddog.android.sample.glidevsfresco

/**
 * ┏┛ ┻━━━━━┛ ┻┓
 * ┃　　　　　　 ┃
 * ┃　　　━　　　┃
 * ┃　┳┛　  ┗┳　┃
 * ┃　　　　　　 ┃
 * ┃　　　┻　　　┃
 * ┃　　　　　　 ┃
 * ┗━┓　　　┏━━━┛
 * * ┃　　　┃   神兽保佑
 * * ┃　　　┃   代码无BUG！
 * * ┃　　　┗━━━━━━━━━┓
 * * ┃　　　　　　　    ┣┓
 * * ┃　　　　         ┏┛
 * * ┗━┓ ┓ ┏━━━┳ ┓ ┏━┛
 * * * ┃ ┫ ┫   ┃ ┫ ┫
 * * * ┗━┻━┛   ┗━┻━┛
 * @author qigengxin
 * @since 2018-07-27 10:49
 */

val CDN_IMG_ARRAY = arrayOf(
        "https://si.geilicdn.com/daily_hz_lucille_00660000016091c78c010a0179bd_2480_3508_unadjust.jpeg",
        "https://si.geilicdn.com/daily_hz_lucille_00610000016091c77ab90a0179bd_850_1200_unadjust.jpeg",
        "https://si.geilicdn.com/daily_hz_lucille_00640000016091c783970a0179bd_992_1403_unadjust.png",
        "https://si.geilicdn.com/daily_hz_lucille_005c0000016091c765670a0179bd_707_1000_unadjust.jpeg",
        "https://si.geilicdn.com/daily_hz_lucille_00630000016091c783600a0179bd_1178_764_unadjust.png",
        "https://si.geilicdn.com/daily_hz_lucille_005b0000016091c761e60a0179bd_720_1280_unadjust.jpeg",
        "https://si.geilicdn.com/daily_hz_lucille_005f0000016091c7752b0a0179bd_1024_1024_unadjust.jpeg",
        "https://si.geilicdn.com/daily_hz_lucille_005d0000016091c7658f0a0179bd_1200_700_unadjust.jpeg",
        "https://si.geilicdn.com/daily_hz_lucille_005a0000016091c761810a0179bd_1192_2048_unadjust.jpeg",
        "https://si.geilicdn.com/daily_hz_lucille_00580000016091c7606a0a0179bd_1514_2048_unadjust.jpeg",
        "https://si.geilicdn.com/daily_hz_lucille_00570000016091c75a070a0179bd_875_614_unadjust.jpeg",
        "https://si.geilicdn.com/daily_hz_lucille_00550000016091c757b10a0179bd_636_2047_unadjust.jpeg",
        "https://si.geilicdn.com/daily_hz_lucille_00560000016091c758b40a0179bd_1100_1600_unadjust.jpeg",
        "https://si.geilicdn.com/daily_hz_lucille_00520000016091c754ec0a0179bd_850_1203_unadjust.jpeg",
        "https://si.geilicdn.com/daily_hz_lucille_00510000016091c7533d0a0179bd_764_1080_unadjust.png",
        "https://si.geilicdn.com/daily_hz_lucille_00500000016091c751380a0179bd_800_1125_unadjust.jpeg",
        "https://si.geilicdn.com/daily_hz_lucille_00540000016091c7579d0a0179bd_610_893_unadjust.png",
        "https://si.geilicdn.com/daily_hz_lucille_00530000016091c756e90a0179bd_675_911_unadjust.png",
        "https://si.geilicdn.com/daily_hz_lucille_00650000016091c7861d0a0179bd_1587_2000_unadjust.png",
        "https://si.geilicdn.com/daily_hz_lucille_004e0000016091c74e020a0179bd_935_1270_unadjust.jpeg",
        "https://si.geilicdn.com/daily_hz_lucille_004a0000016091c743320a0179bd_1000_1412_unadjust.png",
        "https://si.geilicdn.com/daily_hz_lucille_00440000016091c73a010a0179bd_800_800_unadjust.jpeg",
        "https://si.geilicdn.com/daily_hz_lucille_00480000016091c7415e0a0179bd_800_960_unadjust.png",
        "https://si.geilicdn.com/daily_hz_lucille_00430000016091c72ff20a0179bd_572_1200_unadjust.jpeg",
        "https://si.geilicdn.com/daily_hz_lucille_004c0000016091c74be10a0179bd_1200_730_unadjust.png",
        "https://si.geilicdn.com/daily_hz_lucille_00600000016091c777ff0a0179bd_2200_2200_unadjust.jpeg",
        "https://si.geilicdn.com/daily_hz_lucille_00460000016091c73fe30a0179bd_664_888_unadjust.jpeg",
        "https://si.geilicdn.com/daily_hz_lucille_004b0000016091c747370a0179bd_1174_1652_unadjust.jpeg",
        "https://si.geilicdn.com/daily_hz_lucille_00470000016091c7409d0a0179bd_690_981_unadjust.jpeg",
        "https://si.geilicdn.com/daily_hz_lucille_00490000016091c742ce0a0179bd_1080_360_unadjust.jpeg",
        "https://si.geilicdn.com/daily_hz_lucille_004d0000016091c74d5d0a0179bd_690_976_unadjust.jpeg"
)

val GIF_ARRAY = arrayOf(
        "https://si.geilicdn.com/poseidon-00340000016439a3ec830a0283b4-unadjust_1080_389.gif",
        "https://si.geilicdn.com/poseidon-0a55000001644156e9810a026860-unadjust_900_500.gif",
        "https://si.geilicdn.com/poseidon-0b8d00000164415cb83b0a02685e-unadjust_500_452.gif",
        "https://si.geilicdn.com/poseidon-0b8d00000164415cb83b0a02685e-unadjust_500_452.gif",
        "https://si.geilicdn.com/poseidon-21aa000001643b16d8380a028841-unadjust_600_337.gif",
        "https://si.geilicdn.com/poseidon-07ed00000164414fe3700a02685e-unadjust_350_263.gif",
        "https://si.geilicdn.com/poseidon-7c6c0000016439b446670a02685e-unadjust_1080_389.gif",
        "https://si.geilicdn.com/wdbuyerhd-539900000164214610020a028841-unadjust_666_240.gif",
        "https://si.geilicdn.com/poseidon-00370000016439e525050a0283b4-unadjust_666_240.gif",
        "https://si.geilicdn.com/poseidon-005c000001643f1ea6410a0283b4-unadjust_1080_389.gif",
        "https://si.geilicdn.com/daily_hz_lucille_00620000016091c7816d0a0179bd_512_288_unadjust.gif"
)

private var cdnIndex = 0

@Synchronized
fun generateNextCdnUrl():String {
    val ret = CDN_IMG_ARRAY[cdnIndex++]
    cdnIndex %= CDN_IMG_ARRAY.size
    return ret
}

private var gifIndex = 0
@Synchronized
fun generateNextGifUrl():String {
    val ret = GIF_ARRAY[gifIndex++]
    gifIndex %= GIF_ARRAY.size
    return ret
}