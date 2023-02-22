package hello.typeconverter.type;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
//a.equals(b); 라고할때 ip, port가 같은 애들은 다 true가 나온다.
@EqualsAndHashCode
public class IpPort {


    private String ip;
    private int port;

    public IpPort(String ip, int port){
        this.ip = ip;
        this.port = port;
    }
}
