# mall-pay

## 商城支付

1.相关文档

沙箱登录：https://openhome.alipay.com/platform/appDaily.htm 

沙箱环境使用说明：https://doc.open.alipay.com/doc2/detail.htm?treeId=200&articleId=105311&docType=1 

如何使用沙箱环境：https://support.open.alipay.com/support/hotProblemDetail.htm?spm=a219a.7386793.0.0.uS5uZ6&id=251932&tagId=100248 

当面付产品介绍：https://doc.open.alipay.com/docs/doc.htm?spm=a219a.7629140.0.0.hV5Clx&treeId=193&articleId=105072&docType=1 

扫码支付接入指引：https://doc.open.alipay.com/docs/doc.htm?spm=a219a.7629140.0.0.Ia6Wqy&treeId=193&articleId=106078&docType=1 

当面付快速接入：https://doc.open.alipay.com/docs/doc.htm?spm=a219a.7629140.0.0.bROnXf&treeId=193&articleId=105170&docType=1 

当面付接入必读：https://doc.open.alipay.com/docs/doc.htm?spm=a219a.7629140.0.0.hV5Clx&treeId=193&articleId=105322&docType=1 

当面付进阶功能：https://doc.open.alipay.com/docs/doc.htm?spm=a219a.7629140.0.0.YFmkxI&treeId=193&articleId=105190&docType=1 

当面付异步通知-仅用于扫码支付：https://doc.open.alipay.com/docs/doc.htm?spm=a219a.7629140.0.0.BykVSR&treeId=193&articleId=103296&docType=1 

当面付SDK&DEMO：https://support.open.alipay.com/docs/doc.htm?spm=a219a.7386797.0.0.k0rwWc&treeId=193&articleId=105201&docType=1 

服务端SDK：https://doc.open.alipay.com/doc2/detail?treeId=54&articleId=103419&docType=1 

生成RSA密钥：https://doc.open.alipay.com/docs/doc.htm?treeId=291&articleId=105971&docType=1 

线上创建应用说明：https://doc.open.alipay.com/doc2/detail.htm?treeId=200&articleId=105310&docType=1#s0 

2.支付配置文件zfbinfo.properties
```
    
# 1 支付宝网关名、partnerId和appId

# 1.1 支付宝网关
open_api_domain = https://openapi.alipaydev.com/gateway.do
# 1.2 默认即可
mcloud_api_domain = http://mcloudmonitor.com/gateway.do
# 1.3 商户UID
pid=2088102175513647
# 1.4 APPID
appid=2016091300505239

# 2 RSA私钥、公钥和支付宝公钥

# 2.1 通过RSA签名验签工具_MAC_V3生成私钥
private_key = MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCQNzR4lPZFAJN7RBx4IJ9ulfSLW5kf2Yr3Sax/kCgyFLUU2KQwIDFna5EeYFJI0ZlxRQXOYZkdPULQcBmz0BkenzqREstLYztZhkpjc1tlFhKfNxYBkhWOvodmbsBboyUU0YJTtiSTY048QM/WTBJmpJcNjyfLBFtBEhoec00zjZmmqp9qxnIAy4R3jzoquoIbJyzj1Xl/FH+93FHISICHjCHN+w5Q79/gxsPaFEu2MYZkctFzB9szpkb47OR8XqonKoIpYTCFhX5DV3mAINt5t/7cA32KglHCmmtQIf2NAT0JduqpfzNuQHIhRH847SSnS9zdZZ/0ZYxZrBeey0rdAgMBAAECggEAW0y3yUb1CgYtWDJsm5h7lrUUlk0tCb7dfWSE8lU/PUVixonImDcpT8IHo2VzcmpPbCg4Xsnm913bOL5fZzjtTjecClANp/JwlmKwIXnceIiazXVAV0o2n6H7clvXJ6yHQ0DH81symKSb2QVnDtImLBkzEAB7PsnJQ87D/lccnu+oRnIZ/ZCJfEYl3ovykqNAiRKKIoNs2t2pu9v2FLsbmlMBJLzh++OSg+2OqwxmZBgjJpxBhVKURfOhPjlIqbnlhQ+6WDYVkgMWBpjvoMcmZPVQaJp0LfHB1HZqeeHKcDnkkVUkvEa18ZxnsOSY5Kpui6HFV3w9jJkS4AggASZZ+QKBgQDPKvbksxtmcKRrWGRYLMafSASSbFHXfRj/N8wvMfaYxKw/yPW4lBuc4If3PHfPb6JEKRlDEpn/v5kNm5PsVZmPYiOZeZXTgQOshryub71KP8papaJPrSX1TXdFYqasBGVgg/H2sUG3azzPVtXytRTqHy7hR1Skl8rnYuyVJmfvywKBgQCyNYuFm1TdwDVNC6iZXZddB2obrQZIoyf8NrRMfZt2w69s/E7z7GdhbEWk/LYoIuFmi3eEhPSz00Uq8JB4fAzq5UbihFRBgqunGlX4R93BAeFd4qt83W9qf98yUMUyse89dabHBHkMv9sjpxmGf1tfxj6qdAJ2mXAinlkiVM8K9wKBgQC+ejFxS6C4yn7SV/5C+EBTOSNDAacqK1Itbr8k8ZpbEhGRwBL1d+//iI5a6tT5/BlE9EjO266OcDqoQVB7rqS3KbOzrA/u3KNlIKkg9YokV4yVVc9r1Lae2W/9ctvuM0iGuiph+M6Ed2v880lZk9c8ABnZdf1ELHCZ2OHPrPVJiwKBgCmJN2LFqIsi4Il7nSlZ2gyxzLZ0ppFJjelIKs/lOtX8mCezywcpY5MedEEXMbG7J4QKH6pAfvRm2qk+dr3OVAhvtgzXwPef6DhDeCQtQ+9Xa9rBGS06Q1y0PyZwEvZU6KkPgfbDAR/Km938dYOWrYpuH28FKQnRi4RWGfpZgARTAoGBAKkCHZo+kwGQNRCIh2mWLSBUhIPCEi7UPq7Rj7w98uqdaZgu435CTQnBwmmuVXXyqTr700Q3tuOAVcE+kSDZ8mSwVsf+sQuXMDGmlwBEeoim+PzMx60SgXlIdejGv96033q8uS2l/ijnFIEVUW1EZLHswKXgfzFyoT59akUtsnJA
# 2.2 通过RSA签名验签工具_MAC_V3生成公钥
public_key = MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkDc0eJT2RQCTe0QceCCfbpX0i1uZH9mK90msf5AoMhS1FNikMCAxZ2uRHmBSSNGZcUUFzmGZHT1C0HAZs9AZHp86kRLLS2M7WYZKY3NbZRYSnzcWAZIVjr6HZm7AW6MlFNGCU7Ykk2NOPEDP1kwSZqSXDY8nywRbQRIaHnNNM42ZpqqfasZyAMuEd486KrqCGycs49V5fxR/vdxRyEiAh4whzfsOUO/f4MbD2hRLtjGGZHLRcwfbM6ZG+OzkfF6qJyqCKWEwhYV+Q1d5gCDbebf+3AN9ioJRwpprUCH9jQE9CXbqqX8zbkByIUR/OO0kp0vc3WWf9GWMWawXnstK3QIDAQAB

#SHA1withRsa对应支付宝公钥
#alipay_public_key = MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDI6d306Q8fIfCOaTXyiUeJHkrIvYISRcc73s3vF1ZT7XN8RNPwJxo8pWaJMmvyTn9N4HQ632qJBVHf8sxHi/fEsraprwCtzvzQETrNRwVxLO5jVmRGi60j8Ue1efIlzPXV9je9mkjzOmdssymZkh2QhUrCmZYI/FCEa3/cNMW0QIDAQAB

# 3 SHA256withRsa对应支付宝公钥
# 3.1点击"查看支付宝公钥"
alipay_public_key = MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwGEkC82Rk+pBsir0AZ8KsFE+gCDDynqCCRGQ0JMBnDLBXQSEDyBZyCx0yjvk7MBhrQxt6QtFGYj1EGXY64DwhcR2Z/48H5A1wv0As2cPhLEx0tMl2qdTvJm4g22rXqJCppaIKzyOir67S0tQl6ax0qSg9naAWIFmdRSXh33M3Kv4GzeH9dFviq6pjAGl9woN/EGtMKrglfwThhBExUETq3vpmpeC1zw1WligBYNBeILK9weDxNJkDmlVLd6GpGIGr+0H0hf5PdEdIne+J24DlIjkOzR3CdN/XfGdjvcZkaXs7/4kb/PsTzfmBK/Kf+A+5/cGuZpldJzTN7itqwVFAQIDAQAB

# 签名类型: RSA->SHA1withRsa,RSA2->SHA256withRsa（官方推荐）
sign_type = RSA2

# 当面付最大查询次数和查询间隔（毫秒）
max_query_retry = 5
query_duration = 5000

# 当面付最大撤销次数和撤销间隔（毫秒）
max_cancel_retry = 3
cancel_duration = 2000

# 交易保障线程第一次调度延迟和调度间隔（秒）
heartbeat_delay = 5
heartbeat_duration = 900

```
