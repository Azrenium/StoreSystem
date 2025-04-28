package util.sql.response;

import util.ErrorResponse;

public class RowResponse<T> extends ErrorResponse {
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        if(getErrorMessages().size() == 1){
            validate();
            getErrorMessages().clear();
        }

        this.data = data;
    }
}
