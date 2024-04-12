import LocalDateTime from "ts-time/LocalDateTime";

export interface User {
    id?: bigint;
    username?: string;
    email?: string;
    password?: string;
    admin?: boolean;
    registeredAt?: LocalDateTime;
    status?: string;
}

export type TUserList = User[];

export interface UserLoginFormType {
    loginBase: string;
    password: string;
}

export interface UserRegisterFormType {
    username: string;
    email: string;
    password: string;
    repassword: string;
}