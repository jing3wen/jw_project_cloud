import request from "@/utils/request";

export function logout(){
    return request.post('/api/auth/login/userLogout')
}
