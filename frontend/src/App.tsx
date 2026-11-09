import { FormEvent, useEffect, useState } from "react";
import { Building2, Heart, Home as HomeIcon, LogOut, Menu, Plus, Search, ShieldCheck, Upload } from "lucide-react";
import { Link, Navigate, Route, Routes, useNavigate, useParams } from "react-router-dom";
import { AuthSession, Property, request } from "./api";

type Page<T> = { content: T[]; totalElements: number; totalPages: number; number: number };
const money = (value: number) => new Intl.NumberFormat("en-BD", { style: "currency", currency: "BDT", maximumFractionDigits: 0 }).format(value);

function useSession() {
  const [session, setSessionState] = useState<AuthSession | null>(() => {
    const raw = sessionStorage.getItem("estate-session");
    return raw ? (JSON.parse(raw) as AuthSession) : null;
  });
  const setSession = (value: AuthSession | null) => {
    if (value) sessionStorage.setItem("estate-session", JSON.stringify(value));
    else sessionStorage.removeItem("estate-session");
    setSessionState(value);
  };
  return [session, setSession] as const;
}

function Layout({ session, logout }: { session: AuthSession | null; logout: () => void }) {
  return <>
    <header className="site-header">
      <Link className="brand" to="/"><Building2 size={28} /> EstateHub</Link>
      <nav><Link to="/properties?listingType=SALE">Buy</Link><Link to="/properties?listingType=RENT">Rent</Link>{session?.role === "OWNER" && <Link to="/dashboard">My listings</Link>}{session?.role === "ADMIN" && <Link to="/admin">Admin</Link>}</nav>
      <div className="header-actions">{session ? <><Link className="ghost" to="/favorites"><Heart size={18} /></Link>{session.role === "OWNER" && <Link className="button small" to="/list-property"><Plus size={17} /> List property</Link>}<button className="ghost" onClick={logout}><LogOut size={18} /> Sign out</button></> : <Link className="button small" to="/login">Sign in</Link>}<Menu className="mobile-menu" /></div>
    </header>
    <Routes>
      <Route path="/" element={<Home />} /><Route path="/properties" element={<Properties />} /><Route path="/properties/:id" element={<PropertyDetails session={session} />} />
      <Route path="/login" element={<AuthPage mode="login" />} /><Route path="/register" element={<AuthPage mode="register" />} />
      <Route path="/list-property" element={session?.role === "OWNER" ? <PropertyForm /> : <Navigate to="/login" />} />
      <Route path="/dashboard" element={session?.role === "OWNER" ? <Dashboard /> : <Navigate to="/login" />} />
      <Route path="/favorites" element={session ? <Favorites /> : <Navigate to="/login" />} />
      <Route path="/admin" element={session?.role === "ADMIN" ? <Admin /> : <Navigate to="/" />} />
    </Routes>
  </>;
}

function Home() {
  const navigate = useNavigate(); const [query, setQuery] = useState("");
  return <main><section className="hero"><div className="hero-copy"><span className="pill">Verified property marketplace</span><h1>A better way to find your next place.</h1><p>Explore homes, land and commercial spaces for sale or rent. Clear listings, useful filters and owner-managed properties.</p><form className="hero-search" onSubmit={(e) => { e.preventDefault(); navigate("/properties?city=" + encodeURIComponent(query)); }}><Search size={20} /><input value={query} onChange={(e) => setQuery(e.target.value)} placeholder="Search by city, e.g. Dhaka" /><button className="button">Search</button></form></div><div className="hero-art"><div className="building-card"><HomeIcon size={48} /><strong>Find a place that fits</strong><span>Buy • Rent • List</span></div></div></section><section className="feature-strip"><div><ShieldCheck /><strong>Moderated listings</strong><span>Admin review before publishing</span></div><div><Search /><strong>Focused search</strong><span>Filter by location, type and budget</span></div><div><Upload /><strong>Owner listings</strong><span>Create and manage your own properties</span></div></section></main>;
}

function Properties() {
  const params = new URLSearchParams(window.location.search);
  const [data, setData] = useState<Page<Property> | null>(null);
  const [filters, setFilters] = useState({ listingType: params.get("listingType") ?? "", city: params.get("city") ?? "", category: "", minPrice: "", maxPrice: "" });
  const load = () => { const q = new URLSearchParams(Object.entries(filters).filter((entry) => entry[1])); request<Page<Property>>("/properties?" + q).then(setData); };
  useEffect(() => { load(); }, []);
  return <main className="page"><div className="page-heading"><div><span className="eyebrow">Marketplace</span><h1>Find your property</h1></div><span>{data?.totalElements ?? 0} listings</span></div><div className="filter-bar"><select value={filters.listingType} onChange={(e) => setFilters({ ...filters, listingType: e.target.value })}><option value="">Buy & rent</option><option value="SALE">For sale</option><option value="RENT">For rent</option></select><select value={filters.category} onChange={(e) => setFilters({ ...filters, category: e.target.value })}><option value="">All property types</option><option value="HOUSE">House</option><option value="COMMERCIAL">Commercial</option><option value="LAND">Land</option></select><input placeholder="City" value={filters.city} onChange={(e) => setFilters({ ...filters, city: e.target.value })} /><input placeholder="Min price" type="number" value={filters.minPrice} onChange={(e) => setFilters({ ...filters, minPrice: e.target.value })} /><input placeholder="Max price" type="number" value={filters.maxPrice} onChange={(e) => setFilters({ ...filters, maxPrice: e.target.value })} /><button className="button" onClick={load}><Search size={18} /> Apply</button></div><div className="property-grid">{data?.content.map((p) => <PropertyCard key={p.id} property={p} />)}</div>{data?.content.length === 0 && <Empty text="No properties match these filters yet." />}</main>;
}

function PropertyCard({ property }: { property: Property }) {
  return <Link className="property-card" to={"/properties/" + property.id}><div className="property-image"><Building2 size={54} /><span className="tag">{property.listingType === "SALE" ? "For sale" : "For rent"}</span></div><div className="property-body"><h3>{property.title}</h3><p>{property.area}, {property.city}</p><strong>{money(property.price)}</strong><div className="property-meta"><span>{property.category}</span>{property.bedrooms != null && <span>{property.bedrooms} beds</span>}{property.sizeSqFt && <span>{property.sizeSqFt} sq ft</span>}</div></div></Link>;
}

function PropertyDetails({ session }: { session: AuthSession | null }) {
  const { id } = useParams(); const [property, setProperty] = useState<Property | null>(null); const [images, setImages] = useState<{ id: string; url: string }[]>([]); const [message, setMessage] = useState("");
  useEffect(() => { request<Property>("/properties/" + id).then(setProperty); request<{ id: string; url: string }[]>("/properties/" + id + "/images").then(setImages); }, [id]);
  if (!property) return <main className="page"><p>Loading property…</p></main>;
  return <main className="page details"><div className="gallery">{images.length ? images.map((image) => <img key={image.id} src={image.url} alt={property.title} />) : <div className="property-image large"><Building2 size={80} /></div>}</div><div className="details-grid"><article><span className="pill">{property.listingType === "SALE" ? "For sale" : "For rent"}</span><h1>{property.title}</h1><p className="muted">{property.area}, {property.city}</p><h2>{money(property.price)}</h2><p>{property.description}</p><div className="property-meta"><span>{property.category}</span><span>{property.bedrooms ?? "—"} bedrooms</span><span>{property.bathrooms ?? "—"} bathrooms</span><span>{property.sizeSqFt ?? "—"} sq ft</span></div></article><aside className="contact-card"><h3>Interested in this property?</h3>{session ? <><textarea value={message} onChange={(e) => setMessage(e.target.value)} placeholder="Write a message to the property owner…" /><button className="button" onClick={async () => { await request("/properties/" + id + "/inquiries", { method: "POST", body: JSON.stringify({ message }) }); setMessage(""); }}>Send inquiry</button><button className="secondary" onClick={() => request("/favorites/" + id, { method: "POST" })}><Heart size={18} /> Save property</button></> : <Link className="button" to="/login">Sign in to contact owner</Link>}</aside></div></main>;
}

function AuthPage({ mode }: { mode: "login" | "register" }) {
  const [form, setForm] = useState({ email: "", password: "", mobile: "", role: "USER" }); const [error, setError] = useState("");
  const submit = async (e: FormEvent) => { e.preventDefault(); try { const body = mode === "login" ? { email: form.email, password: form.password } : form; const session = await request<AuthSession>("/auth/" + mode, { method: "POST", body: JSON.stringify(body) }); sessionStorage.setItem("estate-session", JSON.stringify(session)); window.location.href = session.role === "OWNER" ? "/dashboard" : "/"; } catch (err) { setError((err as Error).message); } };
  return <main className="auth-page"><form className="auth-card" onSubmit={submit}><span className="eyebrow">EstateHub</span><h1>{mode === "login" ? "Welcome back" : "Create your account"}</h1><p>{mode === "login" ? "Sign in to manage saved properties and listings." : "Browse as a buyer or register as an owner to publish listings."}</p>{error && <div className="error">{error}</div>}<label>Email<input type="email" required value={form.email} onChange={(e) => setForm({ ...form, email: e.target.value })} /></label>{mode === "register" && <><label>Mobile<input value={form.mobile} onChange={(e) => setForm({ ...form, mobile: e.target.value })} /></label><label>Account type<select value={form.role} onChange={(e) => setForm({ ...form, role: e.target.value })}><option value="USER">Buyer / renter</option><option value="OWNER">Property owner</option></select></label></>}<label>Password<input type="password" minLength={8} required value={form.password} onChange={(e) => setForm({ ...form, password: e.target.value })} /></label><button className="button wide">{mode === "login" ? "Sign in" : "Create account"}</button><p className="center">{mode === "login" ? <><span>New here? </span><Link to="/register">Create an account</Link></> : <><span>Already registered? </span><Link to="/login">Sign in</Link></>}</p></form></main>;
}

function PropertyForm() {
  const navigate = useNavigate(); const [form, setForm] = useState({ title: "", description: "", category: "HOUSE", listingType: "SALE", price: "", city: "", area: "", sizeSqFt: "", bedrooms: "", bathrooms: "" });
  const submit = async (e: FormEvent) => { e.preventDefault(); const payload = { ...form, price: Number(form.price), sizeSqFt: form.sizeSqFt ? Number(form.sizeSqFt) : null, bedrooms: form.bedrooms ? Number(form.bedrooms) : null, bathrooms: form.bathrooms ? Number(form.bathrooms) : null }; await request("/properties", { method: "POST", body: JSON.stringify(payload) }); navigate("/dashboard"); };
  return <main className="page narrow"><div className="page-heading"><div><span className="eyebrow">Owner workspace</span><h1>List a property</h1></div></div><form className="form-grid" onSubmit={submit}><label className="full">Title<input required value={form.title} onChange={(e) => setForm({ ...form, title: e.target.value })} /></label><label>Listing type<select value={form.listingType} onChange={(e) => setForm({ ...form, listingType: e.target.value })}><option value="SALE">For sale</option><option value="RENT">For rent</option></select></label><label>Property type<select value={form.category} onChange={(e) => setForm({ ...form, category: e.target.value })}><option>HOUSE</option><option>COMMERCIAL</option><option>LAND</option></select></label><label>Price (BDT)<input required type="number" value={form.price} onChange={(e) => setForm({ ...form, price: e.target.value })} /></label><label>City<input required value={form.city} onChange={(e) => setForm({ ...form, city: e.target.value })} /></label><label>Area<input required value={form.area} onChange={(e) => setForm({ ...form, area: e.target.value })} /></label><label>Size (sq ft)<input type="number" value={form.sizeSqFt} onChange={(e) => setForm({ ...form, sizeSqFt: e.target.value })} /></label><label>Bedrooms<input type="number" value={form.bedrooms} onChange={(e) => setForm({ ...form, bedrooms: e.target.value })} /></label><label>Bathrooms<input type="number" value={form.bathrooms} onChange={(e) => setForm({ ...form, bathrooms: e.target.value })} /></label><label className="full">Description<textarea rows={6} value={form.description} onChange={(e) => setForm({ ...form, description: e.target.value })} /></label><button className="button">Submit for review</button></form></main>;
}

function Dashboard() {
  const [items, setItems] = useState<Property[]>([]); useEffect(() => { request<Property[]>("/properties/mine").then(setItems); }, []);
  return <main className="page"><div className="page-heading"><div><span className="eyebrow">Owner workspace</span><h1>My listings</h1></div><Link className="button" to="/list-property"><Plus size={18} /> Add property</Link></div><div className="table">{items.map((p) => <div className="table-row" key={p.id}><div><strong>{p.title}</strong><span>{p.area}, {p.city}</span></div><span>{p.listingType}</span><span className={"status " + p.status.toLowerCase()}>{p.status.replaceAll("_", " ")}</span><strong>{money(p.price)}</strong></div>)}</div>{items.length === 0 && <Empty text="You have not listed a property yet." />}</main>;
}

function Favorites() {
  const [items, setItems] = useState<Property[]>([]); useEffect(() => { request<Property[]>("/favorites").then(setItems); }, []);
  return <main className="page"><div className="page-heading"><div><span className="eyebrow">Saved</span><h1>Favorite properties</h1></div></div><div className="property-grid">{items.map((p) => <PropertyCard key={p.id} property={p} />)}</div>{items.length === 0 && <Empty text="No saved properties yet." />}</main>;
}

function Admin() {
  const [items, setItems] = useState<Property[]>([]); const load = () => request<Page<Property>>("/admin/properties/pending").then((p) => setItems(p.content)); useEffect(() => { load(); }, []);
  const act = async (id: string, action: string) => { await request("/admin/properties/" + id + "/" + action, { method: "POST" }); load(); };
  return <main className="page"><div className="page-heading"><div><span className="eyebrow">Administration</span><h1>Listing moderation</h1></div></div><div className="table">{items.map((p) => <div className="table-row admin-row" key={p.id}><div><strong>{p.title}</strong><span>{p.area}, {p.city}</span></div><strong>{money(p.price)}</strong><div className="actions"><button className="button small" onClick={() => act(p.id, "approve")}>Approve</button><button className="danger" onClick={() => act(p.id, "reject")}>Reject</button></div></div>)}</div>{items.length === 0 && <Empty text="No listings are waiting for review." />}</main>;
}

function Empty({ text }: { text: string }) { return <div className="empty"><Building2 size={42} /><h3>{text}</h3></div>; }
export function App() { const [session, setSession] = useSession(); return <Layout session={session} logout={() => setSession(null)} />; }
