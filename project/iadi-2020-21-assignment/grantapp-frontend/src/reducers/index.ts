import {combineReducers} from 'redux';
import signInReducer from './LoginReducer';

const RootReducer = combineReducers({
    login: signInReducer
});

export default RootReducer;