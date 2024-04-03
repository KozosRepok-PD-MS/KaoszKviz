import { HttpStatusCode } from "axios";
import { API_KEY_STRING, RESET_TOKEN_STRING } from "./GlobalDatas";

export enum HTTP_METOD {
    GET,
    POST,
    PUT,
    DELETE,
    PATCH
}

export enum API_CONTROLLER {
    USER = "user",
    MEDIA = "media",
}

export const DEFAULT_REQUIRED_HTTP_CODE: number = HttpStatusCode.Ok;

export enum DEFAULT_TYPES {
    NUMBER = "number",
    BIGINT = "bigint",
    STRING = "string",
    BOOLEAN = "boolean",
}

export type RequestVariableProperty = {
    type: DEFAULT_TYPES;
    required: boolean;
    defaultValue: string;
}

export type ApiEndpoint = {
    method: HTTP_METOD;
    url?: string;
    requestBody?: Map<string, RequestVariableProperty>;
    requestParam?: Map<string, RequestVariableProperty>;
    headers?: Map<string, RequestVariableProperty>;
    requiredHttpCode?: number;
}

export const apiEndpoints: Map<API_CONTROLLER, Map<string, ApiEndpoint>> = new Map([
    [
        API_CONTROLLER.USER,
        new Map([
            [
                "create",
                {
                    method: HTTP_METOD.POST,
                    requestBody: new Map([
                        [
                            "username",
                            {
                                type: DEFAULT_TYPES.STRING,
                                required: true,
                                defaultValue: "",
                            }
                        ],
                        [
                            "email",
                            {
                                type: DEFAULT_TYPES.STRING,
                                required: true,
                                defaultValue: "",
                            }
                        ],
                        [
                            "password",
                            {
                                type: DEFAULT_TYPES.STRING,
                                required: true,
                                defaultValue: "",
                            }
                        ],
                    ]),
                }
            ],
            [
                "update",
                {
                    method: HTTP_METOD.PUT,
                    requestBody: new Map([
                        [
                            "id",
                            {
                                type: DEFAULT_TYPES.BIGINT,
                                required: true,
                                defaultValue: "-1"
                            }
                        ],
                        [
                            "username",
                            {
                                type: DEFAULT_TYPES.STRING,
                                required: false,
                                defaultValue: ""
                            }
                        ],
                        [
                            "profilePictureOwner",
                            {
                                type: DEFAULT_TYPES.BIGINT,
                                required: false,
                                defaultValue: "-1"
                            }
                        ],
                        [
                            "profilePictureName",
                            {
                                type: DEFAULT_TYPES.STRING,
                                required: false,
                                defaultValue: ""
                            }
                        ],
                    ]),
                    headers: new Map([
                        [
                            API_KEY_STRING,
                            {
                                type: DEFAULT_TYPES.STRING,
                                required: true,
                                defaultValue: "",
                            }
                        ],
                    ]),
                }
            ],
            [
                "delete",
                {
                    method: HTTP_METOD.DELETE,
                    requestParam: new Map([
                        [
                            "userId",
                            {
                                type: DEFAULT_TYPES.BIGINT,
                                required: true,
                                defaultValue: "-1"
                            }
                        ],
                    ]),
                    headers: new Map([
                        [
                            API_KEY_STRING,
                            {
                                type: DEFAULT_TYPES.STRING,
                                required: true,
                                defaultValue: "",
                            }
                        ],
                    ]),
                }
            ],
            [
                "getAll",
                {
                    method: HTTP_METOD.GET,
                    headers: new Map([
                        [
                            API_KEY_STRING,
                            {
                                type: DEFAULT_TYPES.STRING,
                                required: true,
                                defaultValue: "",
                            }
                        ],
                    ]),
                }
            ],
            [
                "getById",
                {
                    method: HTTP_METOD.GET,
                    requestParam: new Map([
                        [
                            "userId",
                            {
                                type: DEFAULT_TYPES.BIGINT,
                                required: true,
                                defaultValue: "-1"
                            }
                        ],
                    ]),
                    headers: new Map([
                        [
                            API_KEY_STRING,
                            {
                                type: DEFAULT_TYPES.STRING,
                                required: true,
                                defaultValue: "",
                            }
                        ],
                    ]),
                }
            ],
            [
                "searchByName",
                {
                    method: HTTP_METOD.GET,
                    requestParam: new Map([
                        [
                            "searchName",
                            {
                                type: DEFAULT_TYPES.STRING,
                                required: true,
                                defaultValue: "",
                            }
                        ],
                    ]),
                    headers: new Map([
                        [
                            API_KEY_STRING,
                            {
                                type: DEFAULT_TYPES.STRING,
                                required: true,
                                defaultValue: "",
                            }
                        ],
                    ]),
                }
            ],
            [
                "login",
                {
                    method: HTTP_METOD.POST,
                    url: "login",
                    requestBody: new Map([
                        [
                            "loginBase",
                            {
                                type: DEFAULT_TYPES.STRING,
                                required: true,
                                defaultValue: "",
                            }
                        ],
                        [
                            "password",
                            {
                                type: DEFAULT_TYPES.STRING,
                                required: true,
                                defaultValue: "",
                            }
                        ],
                    ]),
                    requiredHttpCode: HttpStatusCode.Created
                }
            ],
            [
                "logout",
                {
                    method: HTTP_METOD.POST,
                    url: "logout",
                    headers: new Map([
                        [
                            API_KEY_STRING,
                            {
                                type: DEFAULT_TYPES.STRING,
                                required: true,
                                defaultValue: "",
                            }
                        ],
                    ]),
                }
            ],
            [
                "changePassword",
                {
                    method: HTTP_METOD.POST,
                    url: "changepassword",
                    requestBody: new Map([
                        [
                            "newPassword",
                            {
	                            type: DEFAULT_TYPES.STRING,
                                required: true,
                                defaultValue: "",
                            }
                        ],
                    ]),
                    headers: new Map([
                        [
                            RESET_TOKEN_STRING,
                            {
                                type: DEFAULT_TYPES.STRING,
                                required: true,
                                defaultValue: "",
                            }
                        ],
                    ]),
                }
            ],
            [
                "resetPassword",
                {
                    method: HTTP_METOD.POST,
                    url: "resetpassword",
                    requestBody: new Map([
                        [
                            "email",
                            {
                                type: DEFAULT_TYPES.STRING,
                                required: true,
                                defaultValue: "",
                            }
                        ],
                    ]),
                }
            ],
        ])
    ],
    [
        API_CONTROLLER.MEDIA,
        new Map<string, ApiEndpoint>([
            [
                "upload",
                {
                    method: HTTP_METOD.POST,
                    
                }
            ]
        ])
    ],
]);