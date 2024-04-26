import axios, { AxiosError, AxiosRequestConfig, AxiosResponse, RawAxiosRequestHeaders } from "axios";
import { API_CONTROLLER, ApiEndpoint, DEFAULT_TYPES, HTTP_METOD, RequestVariableProperty, apiEndpoints } from "../config/ApiEndpoints";
import { API_KEY_STRING } from "../config/GlobalDatas";

export default class ApiHandler {
    static #baseUrl: string ="http://localhost:5556";
    
    static executeApiCall(controller: API_CONTROLLER, method: string, callback: Function, requestBody: Object = new Map<string, string>(), requestParam?: Map<string, string>, headers: Map<string, string> = new Map<string, string>()): void {
        if (!apiEndpoints.has(controller)) {
            throw new Error("controller not found");
        }

        const ENDPOINT: Map<string, ApiEndpoint> = apiEndpoints.get(controller)!;
        if (!ENDPOINT.has(method)) {
            throw new Error("method not found");
        }
        
        const API_ENDPOINT: ApiEndpoint = ENDPOINT.get(method)!;
        
        let url: string = API_ENDPOINT.url ? ApiHandler.buildEndpoint(controller, API_ENDPOINT.url!) : ApiHandler.buildEndpoint(controller);
        url = requestParam ? ApiHandler.buildUrl(url, requestParam) : ApiHandler.buildUrl(url);
        const headersConfig: AxiosRequestConfig<any> = ApiHandler.#createRequestConfig(headers);
        
        if (!ApiHandler.#isValidRequest(requestBody, requestParam ? requestParam : new Map<string, string>() , API_ENDPOINT)) { return; }

        try {
            switch (API_ENDPOINT.method) {
                case HTTP_METOD.POST:
                    console.log("POST: => " + url);
                    axios.post(url, requestBody, headersConfig)
                         .then((result: AxiosResponse<any, any>) => {
                            console.log(result);
                            callback(result);
                         })
                         .catch((error: AxiosError) => {
                            let response: AxiosResponse = error.response!;
                            console.log(response);
                            
                            throw new ApiError(response.status, response.data);
                         });
                    break;
                case HTTP_METOD.PUT:
                    console.log("PUT: => " + url);
                    axios.put(url, requestBody, ApiHandler.#createRequestConfig(headers))
                         .then((result: AxiosResponse<any, any>) => {
                            console.log(result);
                            callback(result);
                         })
                         .catch((error: AxiosError) => {
                            let response: AxiosResponse = error.response!;
                            console.log(response);
                            
                            throw new ApiError(response.status, response.data);
                         });
                    break;
                case HTTP_METOD.DELETE:
                    console.log("DELETE: => " + url);
                    axios.delete(url, ApiHandler.#createRequestConfig(headers))
                         .then((result: AxiosResponse<any, any>) => {
                            console.log(result);
                            callback(result);
                         })
                         .catch((error: AxiosError) => {
                            let response: AxiosResponse = error.response!;
                            console.log(response);
                            
                            throw new ApiError(response.status, response.data);
                         });
                    break;
                case HTTP_METOD.PATCH:
                    console.log("PATCH üres");
                    break;
            
                default:
                    //GET as default
                    console.log("GET: => " + url);
                    axios.get(url, ApiHandler.#createRequestConfig(headers))
                         .then((result: AxiosResponse<any, any>) => {
                            callback(result);
                         })
                         .catch((error) => {
                            console.log(error);
                            
                            throw new Error(error);
                         });
                    break;
            }
        } catch (error) {
            throw error;
        }
    }

    static imageLinkBuild(ownerId?: bigint | string, imageName?: string) {
        if (ownerId === null || ownerId! === -1n || imageName === null || imageName === "") {
            return "/logo192.png";
        }
        return `${this.#baseUrl}/media/${ownerId}/${imageName}`;
    }

    static buildEndpoint(controller: API_CONTROLLER, endpoint?: string): string {
        let endpointStr =  controller.toString();
        endpointStr = endpoint ? (endpointStr + "/" + endpoint) : endpointStr;

        return endpointStr;
    }

    static buildUrl(endpoint: string, parameters?: Map<string, string>): string {
        let url = ApiHandler.#baseUrl + "/" + endpoint;
        let index = 0;
        parameters?.forEach((value: string, key: string) => {
            let symbol = index === 0 ? "?" : "&";
            url += symbol + key + "=" + value;
            index++;
        });

        return url;
    }

    static #isValidRequest(requestData: Object , requestParam: Map<string, string>, apiEndpoint: ApiEndpoint): boolean {

        let REQUIRED_REQUEST_BODY: Map<string, RequestVariableProperty> = apiEndpoint.requestBody ? apiEndpoint.requestBody : new Map<string, RequestVariableProperty>();
        let REQUIRED_REQUEST_PARAM: Map<string, RequestVariableProperty> = apiEndpoint.requestParam ? apiEndpoint.requestParam : new Map<string, RequestVariableProperty>();

        const requestBody = new Map(Object.entries(requestData));

        for (let [key, value] of REQUIRED_REQUEST_BODY) {
            let requirements = new Map(Object.entries(value));
            let valueToCheck = requestBody.get(key);
            
            if (!ApiHandler.#isDataValid(requirements, valueToCheck)) {
                return false;
            }
        }

        for (let [key, value] of REQUIRED_REQUEST_PARAM) {
            let requirements = new Map(Object.entries(value));
            let valueToCheck = requestParam.get(key);
            
            if (!valueToCheck) { valueToCheck = ""; }

            if (!ApiHandler.#isDataValid(requirements, valueToCheck)) {
                return false;
            }
        }

        return true;
    }

    static #isDataValid(requirements: any, valueToCheck: string) {
        if (requirements.get("required") && (typeof valueToCheck === 'string' && valueToCheck.length === 0)) {
            return false;
        }

        switch (requirements.get("type")) {
            case DEFAULT_TYPES.NUMBER:
                if (typeof parseInt(valueToCheck) !== DEFAULT_TYPES.NUMBER) {
                    return false;
                }
                break;
            case DEFAULT_TYPES.BIGINT:
                try {
                    BigInt(valueToCheck);
                } catch(ex) {
                    return false;
                }
                break;
            case DEFAULT_TYPES.BOOLEAN:
                valueToCheck = valueToCheck.toString();
                if (valueToCheck !== "true" && valueToCheck !== "false" && valueToCheck !== "0" && valueToCheck !== "1") {
                    return false;
                }
                break;
            default:
                break;
        }

        return true;
    }

    static #createRequestConfig(headers: Map<string, string>): AxiosRequestConfig {
        let apiKey = localStorage.getItem(API_KEY_STRING);
        if (apiKey) {
            headers.set(API_KEY_STRING, apiKey);
        }
        headers.set("Access-Control-Expose-Headers", "Content-Encoding");

        let config: AxiosRequestConfig = {
            headers: Object.fromEntries(headers) as RawAxiosRequestHeaders,
        };
        return config;
    }
}

export class ApiError extends Error {
    #code: number;
    #message: string;

    constructor(code: number, message: string) {
        super("");
        this.#code = code;
        this.#message = message;
    }

    getCode() {
        return this.#code;
    }

    getMessage() {
        return this.#message;
    }
}