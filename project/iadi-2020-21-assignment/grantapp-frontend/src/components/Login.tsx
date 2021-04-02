import React, {ChangeEvent, FormEvent, useState} from "react";


async function performLogin(username: string, password: string, signIn:(b:boolean)=>void) {
    const myHeaders = new Headers();
    myHeaders.append('Content-Type', 'application/json');

    fetch("/login",
        {
            method: 'POST',
            headers: myHeaders,
            body: JSON.stringify({username: username, password: password})
        })
        .then(response => {
            if (response.ok)
                return response.headers.get('Authorization');
            else {
                console.log(`Error: ${response.status}: ${response.statusText}`)
                return null;
                // and add a message to the Ui: wrong password ?? other errors?
            }
        })
        .catch(err => {
            console.log(err)
        })
        .then(token => {
            if (token) {
                localStorage.setItem('jwt', token);
                // not the safest of ways... but usable for now.
                signIn(true);
            }
        })
}

function performLogout(signIn: (b: boolean) => void) {
    signIn(false);
    localStorage.removeItem('jwt');
}

export function getData<T>(url: string, defaultValue: T): Promise<T> {
    let auth = {};
    let token = localStorage.getItem('jwt');
    if (token) auth = {'Authorization': token};

    // sign out in case of unauthorized access (expired session)
    return fetch(url, {
        method: "GET",
        mode: "cors",
        cache: "no-cache",
        headers: {
            ...auth,
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if (response.ok){
                return response.json();
            }
            else {
                console.log(`Error: ${response.status}: ${response.statusText}`);
                return new Promise<T>((resolve, reject) => resolve(defaultValue))
            }
        })
        .catch(reason => {
            console.log(reason);
        });
}

const SignInForm = (props:{isSignedIn:boolean, signIn:(b:boolean)=>void}) => {

    const [ username, setUsername ] = useState("");
    const [ password, setPassword ] = useState("");

    if( localStorage.getItem('jwt') ) props.signIn(true);
    // and test if it is expired, if yes clean it up

    let submitHandler = (e:FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        performLogin(username, password, props.signIn);
        setUsername("");
        setPassword("")
    };

    let handlerLogout = (e:FormEvent<HTMLButtonElement>) => { performLogout(props.signIn) };

    let usernameChangeHandler = (e:ChangeEvent<HTMLInputElement>) => { setUsername(e.target.value) };

    let passwordChangeHandler = (e:ChangeEvent<HTMLInputElement>) => { setPassword(e.target.value) };

    let signInForm =
        (<form onSubmit={submitHandler}>
            <div><label>Username: <input type="text" value={username} onChange={usernameChangeHandler}/></label></div>
            <div><label>Password: <input type="password" value={password} onChange={passwordChangeHandler}/></label></div>
            <button>Sign In</button>
        </form>);

    let signOutForm = <button onClick={handlerLogout}>Sign out</button>;

    return (<> {props.isSignedIn ? signOutForm : signInForm} </>);
    // add a message space for alerts (you were signed out, expired session)
};

export default SignInForm