1、从官网[href="https://www.rabbitmq.com/community-plugins.html]()"下载延迟插件

2、复制到`rabbitmq plugin`目录下

3、启动插件

```shell 
rabbitmq-plugins enable rabbitmq_delayed_message_exchange
```

4、配置

```yaml
delayed:
  queue: delayed-queue
  exchange: delayed-exchange
  routingKey: delayed-routingKey
```