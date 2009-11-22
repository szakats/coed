package coed.base.data.exceptions;

import java.util.concurrent.ExecutionException;

public class CoedFutureException extends ExecutionException {
	public CoedFutureException(Throwable cause) {
		super(cause);
	}
}
