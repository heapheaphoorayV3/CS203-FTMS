import { Card, Typography } from "@material-tailwind/react";

export default function Table({ tableHead, tableRows }) {
    return (
        <section className="w-full h-full bg-white">
            <Card className="h-full w-full overflow-scroll border border-gray-300 px-6">
                <table className="w-full min-w-max table-auto text-left">
                    <thead>
                        <tr>
                            {tableHead.map((head) => (
                                <th key={head} className="border-b border-gray-300 pb-4 pt-8">
                                    <Typography
                                        variant="small"
                                        color="blue-gray"
                                        className="font-bold leading-none"
                                    >
                                        {head}
                                    </Typography>
                                </th>
                            ))}
                        </tr>
                    </thead>
                    <tbody>
                        {tableRows && tableRows.length > 0 ? (
                            tableRows.map((row, index) => (
                                <tr key={index}>
                                    {row.map((cell, cellIndex) => (
                                        <td key={cellIndex} className="py-4 border-b border-gray-300">
                                            {cell}
                                        </td>
                                    ))}
                                </tr>
                            ))
                        ) : (
                            <tr>
                                <td colSpan={tableHead.length} className="text-center p-4">
                                    No data available.
                                </td>
                            </tr>
                        )}
                    </tbody>
                </table>
            </Card>
        </section>
    );
}
