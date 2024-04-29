import LocalDateTime from "ts-time/LocalDateTime";
import { User } from "./User";


export type Auth = {
    isAuthenticated: boolean;
    user: AuthUser | null;
};

export interface AuthUser {
    id?: bigint;
    username?: string;
    email?: string;
    registeredAt?: LocalDateTime;
    profilePictureOwnerId?: bigint;
    profilePictureName?: string;
}