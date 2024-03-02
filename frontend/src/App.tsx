
import React from 'react';
import './App.css';
import Layout from "./Layout";
import NoPage from "./Pages/NoPage";
import Login from "./Pages/Login";
import Register from "./Pages/Register";
import Game from "./Pages/Game";
import { BrowserRouter, Routes, Route } from "react-router-dom";

function App() {
  return (
    <div className="App">
      <BrowserRouter>
          <Routes>
              <Route path="/" element={<Layout />} >
                  <Route path="" element={<Game />} />
                  <Route path="login" element={<Login />} />
                  <Route path="Register" element={<Register />} />
                  <Route path="*" element={<NoPage />} />
              </Route>
          </Routes>
      </BrowserRouter>
    </div>
  );
}

export default App;