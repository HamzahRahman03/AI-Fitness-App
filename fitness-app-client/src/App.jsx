import { Button } from "@mui/material";
import { useContext, useEffect, useState } from "react";
import { AuthContext } from "react-oauth2-code-pkce";
import { useDispatch } from "react-redux";
import { BrowserRouter as Router, Navigate, Route, Routes, useLocation } from "react-router";
import { setCredentials } from "./store/authSlice";

function App() {
  const { token, tokenData, logIn, logOut, isAuthenticated } = useContext(AuthContext);
  const dispatch = useDispatch();
  const [authReady, setAuthReady] = useState(false);

  useEffect(() => {
    if(token){
      dispatch(setCredentials({token, user: tokenData}));
      setAuthReady(true);
    }
  }, [token, tokenData, dispatch]);

  return (
    <>
    <Button variant="contained" color="#3b8093"
            onClick={() => {
              logIn();
            }}>Login</Button>
    </>
  )
}

export default App