using System.Collections.Generic;
using System.Threading.Tasks;
using MongoDB.Driver;
using usuarios.Models;

namespace usuarios.Repositories
{
    public class RepositorioCodigosActivacion : BaseRepositorio<CodigoActivacion>, IRepositorio<CodigoActivacion>
    {
        public RepositorioCodigosActivacion() : base("CodigoActivacion") { }

        public async Task<IEnumerable<CodigoActivacion>> ObtenerTodosAsync()
        {
            return await _coleccion.Find(_ => true).ToListAsync();
        }

        public async Task<CodigoActivacion> ObtenerPorIdAsync(string id)
        {
            return await _coleccion.Find(c => c.Id == id).FirstOrDefaultAsync();
        }

        public async Task InsertarAsync(CodigoActivacion entidad)
        {
            await _coleccion.InsertOneAsync(entidad);
        }

        public async Task ActualizarAsync(string id, CodigoActivacion entidad)
        {
            await _coleccion.ReplaceOneAsync(c => c.Id == id, entidad);
        }

        public async Task EliminarAsync(string id)
        {
            await _coleccion.DeleteOneAsync(c => c.Id == id);
        }
    }
}
