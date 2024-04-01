import axios, { AxiosError, AxiosRequestConfig, AxiosResponse, RawAxiosRequestHeaders } from "axios";
import { API_CONTROLLER, ApiEndpoint, DEFAULT_TYPES, HTTP_METOD, RequestVariableProperty, apiEndpoints } from "../config/ApiEndpoints";
import { UserLoginFormType } from "../model/User";

export default class ApiHandler {
    static #baseUrl: string ="http://localhost:8000";
    
    static executeApiCall(controller: API_CONTROLLER, method: string, callback: Function, requestBody?: Object, requestParam?: Map<string, string>, headers?: Map<string, string>): void {
        if (!apiEndpoints.has(controller)) {
            throw new Error("controller not found");
        }

        const ENDPOINT: Map<string, ApiEndpoint> = apiEndpoints.get(controller)!;
        if (!ENDPOINT.has(method)) {
            throw new Error("method not found");
        }

        const API_ENDPOINT: ApiEndpoint = ENDPOINT.get(method)!;
        requestBody = requestBody ? requestBody : new Map<string, string>();
        requestParam = requestParam ? requestParam : new Map<string, string>();
        headers = headers ? headers : new Map<string, string>();

        let url = API_ENDPOINT.url ? ApiHandler.buildEndpoint(controller, API_ENDPOINT.url!) : ApiHandler.buildEndpoint(controller);
        
        if (!ApiHandler.#isValidRequest(requestBody, requestParam, API_ENDPOINT)) { return; }

        try {
            switch (API_ENDPOINT.method) {
                case HTTP_METOD.POST:
                    ApiHandler.postRequest(url, callback, requestBody, requestParam, headers)
                    break;
                case HTTP_METOD.PUT:
                    
                    break;
                case HTTP_METOD.DELETE:
                    
                    break;
                case HTTP_METOD.PATCH:
                    
                    break;
            
                default:
                    //GET as default
                    ApiHandler.getRequest(url, callback, requestParam, headers);
                    break;
            }
        } catch (error) {
            throw error;
        }
    }

    static postRequest(endpoint: string, callback: Function, requestBody?: Object, parameters?: Map<string, string>, headers?: Map<string, string>) {
        headers = headers ? headers : new Map<string, string>();
        let url = parameters ? ApiHandler.buildUrl(endpoint, parameters) : ApiHandler.buildUrl(endpoint);
        console.log("POST: => " + url);
        axios.post(url, requestBody, ApiHandler.#createRequestConfig(headers))
             .then(result => {
                console.log(result);
                
                callback(result);
                
             })
             .catch((error: AxiosError) => {
                let response: AxiosResponse = error.response!;
                console.log(response);
                
                throw new ApiError(response.status, response.data);
             });
    }

    static getRequest(endpoint: string, callback: Function, parameters?: Map<string, string>, headers?: Map<string, string>) {
        headers = headers ? headers : new Map<string, string>();
        let url = parameters ? ApiHandler.buildUrl(endpoint, parameters) : ApiHandler.buildUrl(endpoint);
        console.log("GET: => " + url);
        axios.get(url, ApiHandler.#createRequestConfig(headers))
             .then(result => {
                callback(result);
             })
             .catch((error) => {
                throw new Error(error);
             });
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