import React, { useState } from "react";
import { Menu, User, LogOut } from "lucide-react";
import * as DropdownMenu from "@radix-ui/react-dropdown-menu";
import { AnimatePresence, motion } from "framer-motion";

const PAGES = [
  { heading: "Produtos", key: "produtos" },
  { heading: "Clientes", key: "clientes" },
  { heading: "Vendas", key: "vendas" },
  { heading: "Relatórios", key: "relatorios" },
];

export default function Navbar({ page, onNavigate, auth, onLogout }) {
  const [isMobileMenuOpen, setIsMobileMenuOpen] = useState(false);

  return (
    <>
      {/* Top Navbar */}
      <header className="fixed top-0 w-full z-40 bg-[#38210f] text-white shadow-md">
        <div className="mx-auto flex h-16 max-w-7xl items-center justify-between px-4 md:px-8">
          
          {/* MOBILE: Hamburger Left */}
          <div className="flex items-center md:hidden w-1/3">
            <button
              onClick={() => setIsMobileMenuOpen(true)}
              className="p-2 -ml-2 rounded-md hover:bg-white/10 transition-colors"
            >
              <Menu size={24} />
            </button>
          </div>

          {/* DESKTOP: Logo Left | MOBILE: Logo Center */}
          <div className="flex justify-center md:justify-start w-1/3 md:w-auto">
            <div className="flex items-center gap-2 cursor-pointer group" onClick={() => onNavigate("produtos")}>
              <img 
                src="/logo.png" 
                alt="Geladão" 
                className="h-10 w-auto transition-transform duration-300 group-hover:scale-110 group-hover:rotate-3" 
              />
            </div>
          </div>

          {/* DESKTOP: Links Center */}
          <nav className="hidden md:flex flex-1 justify-center gap-6">
            {PAGES.map((p) => (
              <button
                key={p.key}
                onClick={() => onNavigate(p.key)}
                className={`text-sm font-medium uppercase tracking-wider transition-colors hover:text-[#f0be5f] ${
                  page === p.key ? "text-[#f0be5f] border-b-2 border-[#f0be5f]" : "text-white/80"
                }`}
              >
                {p.heading}
              </button>
            ))}
          </nav>

          {/* RIGHT: User Profile */}
          <div className="flex items-center justify-end w-1/3 md:w-auto">
            <DropdownMenu.Root>
              <DropdownMenu.Trigger asChild>
                <button className="flex items-center gap-2 p-2 rounded-full hover:bg-white/10 transition-colors outline-none">
                  <div className="flex h-8 w-8 items-center justify-center rounded-full bg-[#f0be5f] text-[#38210f]">
                    <User size={18} />
                  </div>
                  <span className="hidden md:block text-sm font-medium">
                    {auth?.login || "Usuário"}
                  </span>
                </button>
              </DropdownMenu.Trigger>
              
              <DropdownMenu.Portal>
                <DropdownMenu.Content
                  className="z-50 min-w-[160px] rounded-md bg-white p-2 text-[#38210f] shadow-pop shadow-xl mt-2 animate-in fade-in zoom-in-95 data-[side=bottom]:slide-in-from-top-2"
                  sideOffset={5}
                >
                  <DropdownMenu.Item 
                    className="flex cursor-pointer select-none items-center gap-2 rounded-sm px-3 py-2 text-sm outline-none hover:bg-red-50 hover:text-red-600 focus:bg-red-50 focus:text-red-600 transition-colors"
                    onClick={onLogout}
                  >
                    <LogOut size={16} />
                    Sair do Sistema
                  </DropdownMenu.Item>
                </DropdownMenu.Content>
              </DropdownMenu.Portal>
            </DropdownMenu.Root>
          </div>

        </div>
      </header>

      {/* MOBILE: Sliding Hamburger Menu */}
      <AnimatePresence>
        {isMobileMenuOpen && (
          <>
            {/* Backdrop */}
            <motion.div
              initial={{ opacity: 0 }}
              animate={{ opacity: 1 }}
              exit={{ opacity: 0 }}
              onClick={() => setIsMobileMenuOpen(false)}
              className="fixed inset-0 z-50 bg-black/60 md:hidden"
            />
            {/* Drawer */}
            <motion.div
              initial={{ x: "-100%" }}
              animate={{ x: 0 }}
              exit={{ x: "-100%" }}
              transition={{ type: "spring", bounce: 0, duration: 0.4 }}
              className="fixed inset-y-0 left-0 z-50 w-3/4 max-w-sm bg-[#38210f] text-white shadow-2xl md:hidden"
            >
              <div className="flex flex-col h-full p-6">
                <div className="flex items-center justify-between mb-8 border-b border-white/20 pb-4">
                  <img 
                    src="/logo.png" 
                    alt="Geladão" 
                    className="h-10 w-auto transition-transform duration-300 hover:scale-110 hover:-rotate-3" 
                  />
                  <button
                    onClick={() => setIsMobileMenuOpen(false)}
                    className="p-2 -mr-2 text-white/70 hover:text-white transition-colors"
                  >
                    <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><line x1="18" y1="6" x2="6" y2="18"></line><line x1="6" y1="6" x2="18" y2="18"></line></svg>
                  </button>
                </div>

                <nav className="flex flex-col gap-4 flex-1">
                  {PAGES.map((p) => (
                    <button
                      key={p.key}
                      onClick={() => {
                        onNavigate(p.key);
                        setIsMobileMenuOpen(false);
                      }}
                      className={`text-left text-lg font-medium tracking-wide py-2 transition-colors ${
                        page === p.key ? "text-[#f0be5f]" : "text-white/80 hover:text-white"
                      }`}
                    >
                      {p.heading}
                    </button>
                  ))}
                </nav>
                
                <div className="mt-auto border-t border-white/20 pt-4 flex items-center gap-3">
                  <div className="flex h-10 w-10 items-center justify-center rounded-full bg-[#f0be5f] text-[#38210f]">
                    <User size={20} />
                  </div>
                  <div>
                    <p className="text-sm font-medium">{auth?.login || "Usuário"}</p>
                    <p className="text-xs text-white/50">Logado</p>
                  </div>
                </div>
              </div>
            </motion.div>
          </>
        )}
      </AnimatePresence>
    </>
  );
}
