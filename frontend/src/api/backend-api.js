import axios, {AxiosResponse} from 'axios'

const axiosApi = axios.create({
    baseURL: `/api`,
    timeout: 1000,
    headers: { 'Content-Type': 'application/json' }
});

class User {
    constructor(id, username) {
        this.id = id;
        this.username = username;
    }
}

export default {
    // index() {
    //     return axiosApi.get(`/`);
    // },

    getAuthor(username) {
        return axiosApi.get(`/authors/` + username);
    },

    getArticles() {
        return axiosApi.get('/articles')
    }

//     createUser(firstName: string, lastName: string): Promise<AxiosResponse<number>> {
//         return axiosApi.post(`/user/` + firstName + '/' + lastName);
//     },
//
//     getSecured(user: string, password: string): Promise<AxiosResponse<string>> {
//     return axiosApi.get(`/secured/`,{
//         auth: {
//             username: user,
//             password: password
//         }});
// }
}