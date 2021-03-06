package com.shopmodule.shoppingcart.utils;

public class PayKeys {
    //
    // 请参考 Android平台安全支付服务(msp)应用开发接口(4.2 RSA算法签名)部分，
    // 并使用压缩包中的openssl RSA密钥生成工具，生成一套RSA公私钥。
    // 这里签名时，只需要使用生成的RSA私钥。
    // Note: 为安全起见，使用RSA私钥进行签名的操作过程，应该尽量放到商家服务器端去进行。\

    //合作身份者id，以2088开头的16位纯数字 此id用来支付时快速登录
    public static final String DEFAULT_PARTNER = "2016082000294682";
    //收款支付宝账号
    public static final String DEFAULT_SELLER = "gjqpgl0177@sandbox.com";
    //商户私钥，自助生成，在压缩包中有openssl，用此软件生成商户的公钥和私钥，写到此处要不然服务器返回错误。公钥要传到淘宝合作账户里详情请看淘宝的sdk文档
    public static final String PRIVATE = "MIIEwAIBADANBgkqhkiG9w0BAQEFAASCBKowggSmAgEAAoIBAQD3ZVY6fTGkIpKsA1sJ4Nt8xsuCQkOzV4s0LX8tWLyjCkiYZXJSjacx/TAff0RV4ic5JFIm8ojfxwjyBpinU5pCUOkpxRL0mdy3gqRTkTc+fUxUIT/ofIm8CCiEBQKBCwNSQYupFoAXS2kbevLAK8gCsEp/SpkoE7NHGU5EuJVt7iG/znNFCY0lozD8TuJ4fqODm2IXR0atksgNch4M56DpXTZJD+jbH3NQoRE2RdIpH1q+E3Uj+Qgsexap0EQn3V7ymFcrqLwwCwSpB+5xlxQA74vcv+G7MKLZPanNEWwJxF45k6IqLwOYcGTipIi/+OV+Itn8cBTLPjn0+A4RYkofAgMBAAECggEBAJXPkcXdZiAkp3jul0SGG6OVpkr84Y5Kpwszud217JU2dvuCAxamnOaSkX+hS7pONr5NlknreKM6Hdqhz/MTl0gtQuR+aJoiShv+SpAoUbHPolcXw2F/eNCaiB5aAkguaRZ4hqAf9fUlP0La0wE4AehO5O3sOI6iT1/wnQrUU/i6mH3zgauSfQPZkQVz4Jkbiz5te3ww2uIFec4AUCMCvHZIM0pYdhFHhM7mEqnvmTmPQzQgm7nEf+pF6dHYaXimkVahmW9C7bbNH42i1qoeWLC89c+bk2IEDsZgHCiZSnEgYb9ad0eEBwEXH24SekxbKssdpuyZx8E+ATnKjBE0mZECgYEA/KsVS+WJ6J54RiSqGgSmzUm3dqEjbXONxE4mx72+woacKKDMbzmj6JAPJdh9YZHlASDi2FopR5OxMd1GlzJTLuqx/rdL/B6YEhf1QWlbKnLle38mO60TTUGc3P/ne3lTTDz/yZPvg5LVRd8aRUqIahK4EG79sp+eSLKojHd1LWsCgYEA+qh0rSBicX0GA5syBp4OH4S51hyq4Rc8RZyTgYho7UsGPuKQ5+mdxL2ivtgPP1bPCEaB54KVBxw3DQOI0M4Px8fj+KTlAdp/QMg/yiuDMxXJ7ZseD5pkzWQHvF+fZitoDz2IuJeET+fku4Lw1tMbKJdyQcPVg7uhXxbB4IEwrx0CgYEAsUrS3Lip9kIMHxvnB+6vpGjPhTw4gbqNhWtZJtrrmc+ej5uVF46Yhr1fA61O/UHEMPCEfE93m7P0qAqeV8WGPOuODi9L8BL/neKVw1pMEBMjF272wvUFEDaz/RbLtQZC/sHmselOLULAyBBufKR56AROh6l59vatuFE0l6vS2xUCgYEAz9g1f4VV+/MGtKDQ14nh/iKpkjCNRT5mHPbbnVJGZ1WJpUVrkNI3n3b75zHzWrsZyyvj9dtixdWQlHr0zOfBUBChKYFZ37KVSFuEAZkZHpgXq62vPJLyuRkUDRBhdW/SxqhooWAKN9P6572JBSlh4MPL21qOxsAj7FxLgWS+wMUCgYEAmoTgmjye0VhpU89ejn7ZQafV/0YGpxIo691Tpf9B33NHqDBN5JIlm9XbC8wZzoTIxPFcqvtwHru61HG+TJELUYIwNWaOFY9+9CymoikO8cT2zP/bL9Sv6NiLMtKc3t9W64UOxCeVaJdaz78He9GlxctNutWmtjozmItQOqj8tX4=";
    //公钥
    public static final String PUBLIC = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA92VWOn0xpCKSrANbCeDbfMbLgkJDs1eLNC1/LVi8owpImGVyUo2nMf0wH39EVeInOSRSJvKI38cI8gaYp1OaQlDpKcUS9Jnct4KkU5E3Pn1MVCE/6HyJvAgohAUCgQsDUkGLqRaAF0tpG3rywCvIArBKf0qZKBOzRxlORLiVbe4hv85zRQmNJaMw/E7ieH6jg5tiF0dGrZLIDXIeDOeg6V02SQ/o2x9zUKERNkXSKR9avhN1I/kILHsWqdBEJ91e8phXK6i8MAsEqQfucZcUAO+L3L/huzCi2T2pzRFsCcReOZOiKi8DmHBk4qSIv/jlfiLZ/HAUyz459PgOEWJKHwIDAQAB";
}

