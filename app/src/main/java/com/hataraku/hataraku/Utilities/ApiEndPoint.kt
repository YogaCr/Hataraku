package com.hataraku.hataraku.Utilities

enum class ApiEndPoint(val value: String) {
    BASE_URL("http://api.yogacahya.me/api/"),

    //Auth
    AUTH_REGISTER(BASE_URL.value + "auth/register"),
    AUTH_LOGIN(BASE_URL.value + "auth/login"),
    AUTH_FORGOTPASS(BASE_URL.value + "auth/forgot_password"),
    AUTH_RESETPASS(BASE_URL.value + "auth/reset_password"),
    AUTH_GOOGLE(BASE_URL.value + "auth/google"),

    //User
    USER(BASE_URL.value + "user"),

    //Member
    MEMBER(BASE_URL.value + "member"),

    //Kategori
    KATEGORI(BASE_URL.value + "kategori"),

    //Handyman
    HANDYMAN(BASE_URL.value + "handyman"),

    //Lowongan
    LOWONGAN(BASE_URL.value + "lowongan"),

    //Tawaran
    TAWARAN(BASE_URL.value + "tawaran"),

    //Transaksi
    TRANSAKSI(BASE_URL.value + "transaksi"),

    //Portofolio
    PORTOFOLIO(BASE_URL.value + "portfolio"),

    //Review
    REVIEW(BASE_URL.value + "review")
}