using System.Collections.Generic;
using System.Threading.Tasks;

namespace usuarios.Repositories
{
    public interface IRepositorio<T>
    {
        Task<IEnumerable<T>> ObtenerTodosAsync();
        Task<T> ObtenerPorIdAsync(string id);
        Task InsertarAsync(T entidad);
        Task ActualizarAsync(string id, T entidad);
        Task EliminarAsync(string id);
    }
}
