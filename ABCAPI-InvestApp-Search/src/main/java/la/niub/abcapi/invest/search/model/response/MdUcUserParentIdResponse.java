package la.niub.abcapi.invest.search.model.response;

import java.io.Serializable;

import lombok.Data;

@Data
public class MdUcUserParentIdResponse implements Serializable {
	private static final long serialVersionUID = 141462523628061215L;

	private Integer code;

	private MdUcUserParentIdDataResponse data;

	private String message;

	private Boolean success;

}
