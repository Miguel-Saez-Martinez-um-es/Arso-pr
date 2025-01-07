using System.Collections.Generic;
using System.Threading.Tasks;
using MongoDB.Driver;
using usuarios.Models;

namespace usuarios.Repositories
{
    public class RepositorioUsuarios : BaseRepositorio<UsuarioModelo>, IRepositorio<UsuarioModelo>
    {
        public RepositorioUsuarios() : base("usuario") { }

        public async Task<IEnumerable<UsuarioModelo>> ObtenerTodosAsync()
        {
            return await _coleccion.Find(_ => true).ToListAsync();
        }

        public async Task<UsuarioModelo> ObtenerPorIdAsync(string id)
        {
            return await _coleccion.Find(u => u.Id == id).FirstOrDefaultAsync();
        }

        public async Task InsertarAsync(UsuarioModelo entidad)
        {
            await _coleccion.InsertOneAsync(entidad);
        }

        public async Task ActualizarAsync(string id, UsuarioModelo entidad)
        {
            await _coleccion.ReplaceOneAsync(u => u.Id == id, entidad);
        }

        public async Task EliminarAsync(string id)
        {
            await _coleccion.DeleteOneAsync(u => u.Id == id);
        }
    }
}
