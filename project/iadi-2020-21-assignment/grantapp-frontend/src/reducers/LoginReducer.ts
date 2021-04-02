import {Action} from "redux";
import {SIGN_IN, SIGN_OUT, SignInAction} from "../actions/SignInAction";

function checkIfTokenIsValid() {
    return localStorage.getItem('jwt') != null;
}

function decodeUsername() {
    const jwt = require("jsonwebtoken");
    const token = localStorage.getItem('jwt');
    if (token) {
        const t = token.slice(7, token.length);
        const decode = jwt.decode(t);
        return decode.username
    } else {
        return ""
    }
}

function decodeRole() {
    const jwt = require("jsonwebtoken");
    const token = localStorage.getItem('jwt');
    if (token) {
        const t = token.slice(7, token.length);
        const decode = jwt.decode(t);

        return decode.roles[0].authority
    } else {
        return ""
    }
}

function decodeId() {
    const jwt = require("jsonwebtoken");
    const token = localStorage.getItem('jwt');
    if (token) {
        const t = token.slice(7, token.length);
        const decode = jwt.decode(t);

        return decode.userid
    } else {
        return ""
    }
}


const initialState = {isSignedIn: checkIfTokenIsValid(), currentUser: decodeUsername(), currentRole: decodeRole(), currentId: decodeId()};

function signInReducer(state = initialState, action: Action) {
    switch (action.type) {
        case SIGN_IN:

            let token = (action as SignInAction).data;
            if (token) {
                localStorage.setItem('jwt', token);
                const username = decodeUsername();
                const role = decodeRole();
                const id = decodeId();
                return {...state, isSignedIn: true, currentUser: username, currentRole: role, currentId: id};
            } else {
                return state;
            }
        case SIGN_OUT:

            localStorage.removeItem('jwt');
            return {...state, isSignedIn: false, currentUser: "", currentRole: "", currentId: ""};
        default:
            return state;
    }
}

export default signInReducer