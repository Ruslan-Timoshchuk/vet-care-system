package com.system.vetcare.controller.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class AuthenticationUrl {

	public final String ABSOLUTE_API_PATH = "/";
	public final String SECURITY_API_PATH = "/api/v1/security";
	public final String USER_REGISTRATION = "/registration";
	public final String USER_LOGIN = "/login";
	public final String USER_LOGOUT = "/logout";
	public final String REFRESH_JWT_TOKEN = "/refresh";
	public final String VALIDATE_EMAIL = "/validate_email";

}