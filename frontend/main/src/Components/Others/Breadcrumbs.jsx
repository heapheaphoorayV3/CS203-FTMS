export default function Breadcrumbs({ items }) {
  return (
    <div className="breadcrumbs text-sm mt-6 mb-0 ml-12">
      <ul>
        {items.map((item, index) => (
          <li key={index}>
            {item.link ? (
              <a href={item.link}>{item.name}</a>
            ) : (
              item.name
            )}
          </li>
        ))}
      </ul>
    </div>
  );
}
