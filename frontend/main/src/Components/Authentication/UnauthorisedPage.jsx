import { motion } from "framer-motion";

export default function UnauthorisedPage() {
    // Animation variants for the main text
    const textVariants = {
        initial: { y: -100, opacity: 0 },
        animate: { 
            y: 0, 
            opacity: 1,
            transition: {
                duration: 0.8,
                ease: "backOut"
            }
        }
    };

    // Draggable constraints
    const dragConstraints = {
        top: -50,
        left: -50,
        right: 50,
        bottom: 50,
    };

    return (
        <div className="h-full w-full flex flex-col items-center justify-center bg-gradient-to-b from-gray-50 to-gray-100 relative overflow-hidden">
            {/* Floating lock icons in background */}
            <div className="absolute inset-0 overflow-hidden">
                {[...Array(12)].map((_, i) => (
                    <motion.div
                        key={i}
                        className="absolute text-gray-200 text-opacity-20 select-none cursor-pointer"
                        style={{
                            top: `${Math.random() * 100}%`,
                            left: `${Math.random() * 100}%`,
                            fontSize: `${Math.random() * 40 + 10}px`
                        }}
                        drag
                        dragConstraints={{ left: 0, right: 0, top: 0, bottom: 0 }}
                        whileHover={{ scale: 1.2, color: "#DC2626" }}
                        whileDrag={{ scale: 1.1 }}
                        animate={{
                            y: [0, -30, 0],
                            opacity: [0.1, 0.3, 0.1]
                        }}
                        transition={{
                            duration: 3 + Math.random() * 2,
                            repeat: Infinity,
                            delay: Math.random() * 2
                        }}
                    >
                        🔒
                    </motion.div>
                ))}
            </div>

            {/* Main content */}
            <div className="z-10 text-center">
                <motion.div
                    variants={textVariants}
                    initial="initial"
                    animate="animate"
                    className="relative"
                >
                    {/* Main lock icon */}
                    <motion.div 
                        className="text-8xl mb-6 cursor-pointer"
                        whileHover={{ scale: 1.1, rotate: [0, -10, 10, 0] }}
                        drag
                        dragConstraints={dragConstraints}
                        whileDrag={{ scale: 0.95 }}
                    >
                        🔒
                    </motion.div>

                    {/* Interactive title */}
                    <motion.h1 
                        className="text-6xl font-bold text-red-600 mb-4 cursor-pointer"
                        whileHover={{ scale: 1.05 }}
                        drag
                        dragConstraints={dragConstraints}
                        whileDrag={{ scale: 0.95 }}
                    >
                        Restricted Access
                    </motion.h1>

                    {/* Background text effect */}
                    <motion.div
                        className="absolute top-0 left-1/2 transform -translate-x-1/2 text-[200px] font-bold text-red-100 z-[-1] select-none"
                        animate={{ 
                            rotate: [0, 5, -5, 0],
                            y: [0, -20, 0]
                        }}
                        transition={{ 
                            duration: 6,
                            repeat: Infinity,
                            ease: "easeInOut"
                        }}
                    >
                        🔒
                    </motion.div>
                </motion.div>

                <motion.div
                    initial={{ opacity: 0, y: 20 }}
                    animate={{ opacity: 1, y: 0 }}
                    transition={{ delay: 0.5 }}
                    className="space-y-6"
                >
                    {/* Message */}
                    <motion.p 
                        className="text-xl text-gray-600 max-w-md mx-auto"
                        whileHover={{ color: "#DC2626" }}
                    >
                        You do not have permission to access this page. 
                        Are you logged in correctly?
                    </motion.p>

                    {/* Additional info */}
                    <motion.div
                        className="text-gray-500 text-sm mt-4"
                        initial={{ opacity: 0 }}
                        animate={{ opacity: 1 }}
                        transition={{ delay: 1 }}
                    >
                    </motion.div>
                </motion.div>
            </div>
        </div>
    );
}
