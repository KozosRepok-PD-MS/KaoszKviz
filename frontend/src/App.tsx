
import React from 'react';
import './App.css';
import Layout from "./Layout";
import NoPage from "./pages/NoPage";
import Login from "./pages/Login";
import Register from "./pages/Register";
import Game from "./pages/Game";
import CreateQuiz from "./pages/CreateQuiz";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import { AuthProvider } from "./context/AuthContext";
import UsersList from './pages/UsersList';
import UserDatas from './pages/UserDatas';
import QuestionList from './pages/QuestionList';
import UserPage from './pages/UserPage';
import AuthUserQuizes from './pages/AuthUserQuizes';

function App() {
    return (
        <div className="App">
            <AuthProvider>
                <BrowserRouter>
                    <Routes>
                        <Route path="/" element={<Layout />} >
                            <Route path="" element={<Game />} />
                            <Route path="login" element={<Login />} />
                            <Route path="register" element={<Register />} />
                            <Route path="users" element={<UsersList />} />
                            <Route path="user/:id" element={<UserPage />} />
                            <Route path="myquizes" element={<AuthUserQuizes />} />
                            <Route path="newquiz" element={<CreateQuiz quizId={-1n} isnew={true}/>} />
                            <Route path="quiz/:id" element={<QuestionList />} />
                            <Route path="profile" element={<UserDatas />} />
                            <Route path="*" element={<NoPage />} />
                        </Route>
                    </Routes>
                </BrowserRouter>
            </AuthProvider>
        </div>
    );
}

export default App;