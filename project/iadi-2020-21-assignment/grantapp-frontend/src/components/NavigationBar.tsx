import {Nav, Navbar} from "react-bootstrap";
import logo from "../logo.svg";
import {FaSignInAlt} from "react-icons/fa";
import React, {useState} from "react";
import LoginModal from "./LoginModal";
import {useDispatch, useSelector} from "react-redux";
import {RootStore} from "../Store";
import {signOut} from "../actions/SignInAction";
import {Link} from "react-router-dom";
import '../css/routerpage.css';
import {AiOutlineUserAdd, FiLogIn} from "react-icons/all";

function NavigationBar() {
    const dispatch = useDispatch();
    const login = useSelector((state: RootStore) => state.login);
    const HandleSignOut = () => dispatch(signOut());
    const [show, setShow] = useState(false);
    let showModal = () => setShow(true);
    let hideModal = () => setShow(false);
    var role = login.currentRole.replace('ROLE_', '');

    return (
        <>
            <Navbar bg="light" expand="lg">
                <Navbar.Brand href="#home">
                    <img
                        src={logo}
                        width="32"
                        height="36"
                        className="d-inline-block align-top"
                        alt="React Bootstrap logo"
                    />
                </Navbar.Brand>
                <Navbar.Brand href="/">Grant Call System</Navbar.Brand>
                <Navbar.Toggle aria-controls="basic-navbar-nav"/>
                {/* eslint-disable-next-line eqeqeq */}
                {role == 'REVIEWER' ? (
                    <Nav className="center-links">
                        <div>
                            <Link to='/reviewer'>Review Applications</Link>
                            <Link to='/chair'>Manage Applications State</Link>
                        </div>
                    </Nav>) : null}

                {role == 'STUDENT' ? (
                    <Nav className="center-links">
                        <div>
                            <Link to='/myapplications'>My Applications</Link>
                        </div>
                    </Nav>
                ) : null}
                <Navbar.Collapse className="justify-content-end">
                    {!login.isSignedIn && <>
                        <Navbar.Text>
                            <button className="sign-in-button" onClick={showModal}><AiOutlineUserAdd/> Register</button>
                            <button className="sign-in-button" onClick={showModal}><FiLogIn/> Sign in</button>
                        </Navbar.Text> </>}
                    {login.isSignedIn && <>
                        <Navbar.Text>
                            [{role}] {login.currentUser} &nbsp;&nbsp;&nbsp; | &nbsp;&nbsp;&nbsp;
                            <button className="sign-out-button" onClick={HandleSignOut}><FaSignInAlt/> Sign out</button>
                        </Navbar.Text> </>}
                </Navbar.Collapse>
            </Navbar>
            <LoginModal show={show} handleClose={hideModal}/>
        </>
    );
}

export default NavigationBar;