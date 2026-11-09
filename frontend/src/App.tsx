import {Link, Route, Routes} from "react-router-dom";
const Home=()=> <main><section className="hero"><p className="eyebrow">Find your next place</p><h1>Property search without the noise.</h1><p>Browse verified homes, commercial properties and land for sale or rent.</p><Link className="button" to="/properties">Browse properties</Link></section></main>;
const Properties=()=> <main><h1>Properties</h1><p>Search and filters will connect to the public property API.</p></main>;
const Login=()=> <main><h1>Sign in</h1><p>Authentication UI foundation. OTP remains a future integration.</p></main>;
export function App(){return <><header><Link className="brand" to="/">EstateHub</Link><nav><Link to="/properties">Properties</Link><Link to="/login">Sign in</Link></nav></header><Routes><Route path="/" element={<Home/>}/><Route path="/properties" element={<Properties/>}/><Route path="/login" element={<Login/>}/></Routes></>}
