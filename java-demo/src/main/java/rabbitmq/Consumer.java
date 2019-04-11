package rabbitmq;

import com.rabbitmq.client.*;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
public class Consumer {

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("admin");
        factory.setPassword("admin");
        factory.setHost("localhost");
        //���������������������
        Connection conn = factory.newConnection();
        //����ŵ�
        final Channel channel = conn.createChannel();
        //����������
        String exchangeName = "hello-exchange";
        channel.exchangeDeclare(exchangeName, "direct", true);
        //��������
        String queueName = channel.queueDeclare().getQueue();
        String routingKey = "hola";
        //�󶨶��У�ͨ���� hola �����кͽ�����������
        channel.queueBind(queueName, exchangeName, routingKey);

        while(true) {
            //������Ϣ
            boolean autoAck = false;
            String consumerTag = "";
            channel.basicConsume(queueName, autoAck, consumerTag, new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag,
                                           Envelope envelope,
                                           AMQP.BasicProperties properties,
                                           byte[] body) throws IOException {
                    String routingKey = envelope.getRoutingKey();
                    String contentType = properties.getContentType();
                    System.out.println("���ѵ�·�ɼ���" + routingKey);
                    System.out.println("���ѵ��������ͣ�" + contentType);
                    long deliveryTag = envelope.getDeliveryTag();
                    //ȷ����Ϣ
                    channel.basicAck(deliveryTag, false);
                    System.out.println("���ѵ���Ϣ�����ݣ�");
                    String bodyStr = new String(body, "UTF-8");
                    System.out.println(bodyStr);

                }
            });
        }
    }
}