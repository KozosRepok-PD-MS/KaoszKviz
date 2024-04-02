

export type Auth = {
    isAuthenticated: boolean;
    user: AuthUser | null;
};

export interface AuthUser {
    id?: bigint;
    username?: string;
    email?: string;
}