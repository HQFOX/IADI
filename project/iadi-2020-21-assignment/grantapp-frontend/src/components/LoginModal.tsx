import * as React from 'react';
import {ChangeEvent, MouseEvent, useState} from 'react';
import {Button, Form, Modal} from 'react-bootstrap';
import {requestSignIn} from "../actions/SignInAction";
import {useDispatch, useSelector} from "react-redux";
import {RootStore} from "../Store";

export interface Props {
    show: boolean;
    handleClose: () => void
}

function LoginModal({show, handleClose}: Props) {
    const dispatch = useDispatch();
    const login = useSelector((state: RootStore) => state.login);

    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");

    let submitHandler = (e: MouseEvent<HTMLButtonElement>) => {
        e.preventDefault();
        dispatch(requestSignIn(username, password));
        setUsername("");
        setPassword("")
    };

    let usernameChangeHandler = (e: ChangeEvent<HTMLInputElement>) => {
        setUsername(e.target.value)
    };

    let passwordChangeHandler = (e: ChangeEvent<HTMLInputElement>) => {
        setPassword(e.target.value)
    };

    if (show && !login.isSignedIn)
        return (
            <Modal show={show} onHide={handleClose}>
                <Modal.Header closeButton>
                    <Modal.Title>Sign In</Modal.Title>
                </Modal.Header>
                <Form>
                    <Modal.Body>

                        <Form.Group controlId="formBasicEmail">
                            <Form.Label>Username:</Form.Label>
                            <Form.Control placeholder="Enter Username" value={username}
                                          onChange={usernameChangeHandler}/>
                        </Form.Group>
                        <Form.Group controlId="formBasicPassword">
                            <Form.Label>Password</Form.Label>
                            <Form.Control type="password" placeholder="Password" value={password}
                                          onChange={passwordChangeHandler}/>
                        </Form.Group>
                    </Modal.Body>
                    <Modal.Footer>
                        <Button variant="primary" onClick={submitHandler}>
                            Log in
                        </Button>
                    </Modal.Footer>
                </Form>

            </Modal>
        )
    else
        return (<></>)
}

export default LoginModal;