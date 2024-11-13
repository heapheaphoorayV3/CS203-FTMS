import { motion } from "framer-motion";

export default function PageNotFound() {
    // Animation variants for the numbers
    const numberVariants = {
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
        top: -100,
        left: -100,
        right: 100,
        bottom: 100,
    };

    return (
        <div className="h-full w-full flex flex-col items-center justify-center bg-gradient-to-b from-gray-50 to-gray-100 relative overflow-hidden">
            {/* Interactive floating elements */}
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
                        whileHover={{ scale: 1.2, color: "#4F46E5" }}
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
                        404
                    </motion.div>
                ))}
            </div>

            {/* Main content */}
            <div className="z-10 text-center">
                <motion.div
                    variants={numberVariants}
                    initial="initial"
                    animate="animate"
                    className="relative"
                >
                    {/* Interactive main 404 */}
                    <motion.h1 
                        className="text-9xl font-bold text-indigo-600 mb-4 cursor-pointer"
                        whileHover={{ scale: 1.1 }}
                        drag
                        dragConstraints={dragConstraints}
                        whileDrag={{ scale: 0.95 }}
                    >
                        404
                    </motion.h1>

                    {/* Interactive background 404 */}
                    <motion.div
                        className="absolute top-0 left-1/2 transform -translate-x-1/2 text-[300px] font-bold text-indigo-100 z-[-1] select-none"
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
                        404
                    </motion.div>
                </motion.div>

                <motion.div
                    initial={{ opacity: 0, y: 20 }}
                    animate={{ opacity: 1, y: 0 }}
                    transition={{ delay: 0.5 }}
                    className="space-y-6"
                >
                    {/* Interactive title */}
                    <motion.h2 
                        className="text-4xl font-semibold text-gray-700 cursor-pointer"
                        whileHover={{ scale: 1.05 }}
                        whileTap={{ scale: 0.95 }}
                    >
                        Oops! Page Not Found
                    </motion.h2>
                    <motion.p 
                        className="text-gray-600 text-lg max-w-md mx-auto"
                        whileHover={{ color: "#4F46E5" }}
                    >
                        Where are you trying to go, buddy? This page doesn't exist.
                    </motion.p>
                </motion.div>
            </div>
        </div>
    );
}