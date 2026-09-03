import React, { useState, useRef } from "react";
import { motion, useMotionValue, AnimatePresence } from "framer-motion";

const MENU_SLIDE_ANIMATION = {
  initial: { x: "calc(100% + 100px)" },
  enter: { x: "0", transition: { duration: 0.8, ease: [0.76, 0, 0.24, 1] } },
  exit: {
    x: "calc(100% + 100px)",
    transition: { duration: 0.8, ease: [0.76, 0, 0.24, 1] },
  },
};

const defaultNavItems = [
  { heading: "Produtos", key: "produtos" },
  { heading: "Clientes", key: "clientes" },
  { heading: "Vendas", key: "vendas" },
  { heading: "Relatórios", key: "relatorios" },
];

const NavLink = ({ heading, pageKey, setIsActive, index, onNavigate }) => {
  const ref = useRef(null);
  const x = useMotionValue(0);
  const y = useMotionValue(0);

  const handleMouseMove = (e) => {
    const rect = ref.current.getBoundingClientRect();
    const mouseX = e.clientX - rect.left;
    const mouseY = e.clientY - rect.top;
    x.set(mouseX / rect.width - 0.5);
    y.set(mouseY / rect.height - 0.5);
  };

  const handleClick = () => {
    onNavigate(pageKey);
    setIsActive(false);
  };

  return (
    <motion.div
      onClick={handleClick}
      initial="initial"
      whileHover="whileHover"
      className="group relative flex items-center justify-between border-b border-black/30 py-4 transition-colors duration-500 md:py-8 uppercase cursor-pointer"
    >
      <div ref={ref} onMouseMove={handleMouseMove} className="w-full">
        <div className="relative flex items-start">
          <span className="text-black transition-colors duration-500 text-4xl font-thin mr-2">
            0{index}.
          </span>
          <div className="flex flex-row gap-2">
            <motion.span
              variants={{
                initial: { x: 0 },
                whileHover: { x: -16 },
              }}
              transition={{
                type: "spring",
                staggerChildren: 0.075,
                delayChildren: 0.25,
              }}
              className="relative z-10 block text-4xl font-extralight text-black transition-colors duration-500 md:text-4xl"
            >
              {heading.split("").map((letter, i) => {
                return (
                  <motion.span
                    key={i}
                    variants={{
                      initial: { x: 0 },
                      whileHover: { x: 16 },
                    }}
                    transition={{ type: "spring" }}
                    className="inline-block"
                  >
                    {letter}
                  </motion.span>
                );
              })}
            </motion.span>
          </div>
        </div>
      </div>
    </motion.div>
  );
};

const Curve = () => {
  const initialPath = `M100 0 L200 0 L200 ${window.innerHeight} L100 ${window.innerHeight} Q-100 ${window.innerHeight / 2} 100 0`;
  const targetPath = `M100 0 L200 0 L200 ${window.innerHeight} L100 ${window.innerHeight} Q100 ${window.innerHeight / 2} 100 0`;

  const curve = {
    initial: { d: initialPath },
    enter: {
      d: targetPath,
      transition: { duration: 1, ease: [0.76, 0, 0.24, 1] },
    },
    exit: {
      d: initialPath,
      transition: { duration: 0.8, ease: [0.76, 0, 0.24, 1] },
    },
  };

  return (
    <svg
      className="absolute top-0 -left-[99px] w-[100px] stroke-none h-full"
      style={{ fill: "#ffffff" }}
    >
      <motion.path
        variants={curve}
        initial="initial"
        animate="enter"
        exit="exit"
      />
    </svg>
  );
};

const CurvedNavbar = ({ setIsActive, onNavigate, onLogout }) => {
  return (
    <motion.div
      variants={MENU_SLIDE_ANIMATION}
      initial="initial"
      animate="enter"
      exit="exit"
      className="h-[100dvh] w-screen max-w-screen-sm fixed right-0 top-0 z-40 bg-white"
    >
      <div className="h-full pt-11 flex flex-col justify-between">
        <div className="flex flex-col text-5xl gap-3 mt-0 px-10 md:px-24">
          <div className="text-black border-b border-black/30 uppercase text-sm mb-0 pb-2">
            <p>Navegação</p>
          </div>
          <section className="bg-transparent mt-0">
            <div className="mx-auto max-w-7xl">
              {defaultNavItems.map((item, index) => {
                return (
                  <NavLink
                    key={item.key}
                    heading={item.heading}
                    pageKey={item.key}
                    setIsActive={setIsActive}
                    index={index + 1}
                    onNavigate={onNavigate}
                  />
                );
              })}
            </div>
          </section>
        </div>
        <div className="flex w-full text-sm justify-between text-black px-10 md:px-24 py-10">
          <button 
            onClick={onLogout} 
            className="text-red-600 hover:text-red-800 uppercase font-semibold tracking-wider transition-colors"
          >
            Sair do Sistema
          </button>
        </div>
      </div>
      <Curve />
    </motion.div>
  );
};

export default function Sidebar({ page, onNavigate, auth, onLogout }) {
  const [isActive, setIsActive] = useState(false);

  const handleClick = () => {
    setIsActive(!isActive);
  };

  return (
    <>
      <div className="relative">
        <div
          onClick={handleClick}
          className="fixed right-0 top-0 m-5 z-50 w-12 h-12 rounded-none flex items-center justify-center cursor-pointer bg-transparent"
        >
          <div className="relative w-8 h-6 flex flex-col justify-between items-center">
            <span
              className={`block h-1 w-7 bg-black transition-transform duration-300 ${
                isActive ? "rotate-45 translate-y-2.5" : ""
              }`}
            ></span>
            <span
              className={`block h-1 w-7 bg-black transition-opacity duration-300 ${
                isActive ? "opacity-0" : ""
              }`}
            ></span>
            <span
              className={`block h-1 w-7 bg-black transition-transform duration-300 ${
                isActive ? "-rotate-45 -translate-y-2.5" : ""
              }`}
            ></span>
          </div>
        </div>
      </div>

      <AnimatePresence mode="wait">
        {isActive && (
          <CurvedNavbar
            setIsActive={setIsActive}
            onNavigate={onNavigate}
            onLogout={onLogout}
          />
        )}
      </AnimatePresence>
    </>
  );
}
