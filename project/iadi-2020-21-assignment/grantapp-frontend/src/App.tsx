import React from 'react';
import NavigationBar from "./components/NavigationBar"
import {BrowserRouter as Router, Route, Switch} from 'react-router-dom'
import MainPage from "./pages"
import NotFoundPage from "./pages/404";
import StudentPage from "./pages/StudentPage";
import ReviewerPage from "./pages/ReviewerPage";
import GrantDetails from "./pages/GrantDetails";
import StudentApplications from "./pages/StudentApplications";
import ChairPage from "./pages/ChairPage";

const App = () => {

    return (
        <Router>
            <NavigationBar/>
            <Switch>
                <Route path="/" exact component={MainPage}/>
                <Route path="/grant/:id" exact component={GrantDetails} />
                <Route path="/myapplications" component={StudentApplications}/>
                <Route path="/reviewer" component={ReviewerPage}/>
                <Route path="/chair" component={ChairPage}/>
                <Route component={NotFoundPage}/>
            </Switch>
        </Router>);
};

export default App;