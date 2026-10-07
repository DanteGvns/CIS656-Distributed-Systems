import java.io.Serializable;

public class RpcRequestHandler implements Serializable {

    private static final long serialVersionUID = 7503710091945320739L;

    private String method;
    private String message;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
