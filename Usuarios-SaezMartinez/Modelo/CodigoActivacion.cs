using System;
using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;

namespace usuarios.Models
{
    public class CodigoActivacion
    {
        [BsonId]
        [BsonRepresentation(BsonType.ObjectId)]
        public string Id { get; set; }

        public string UsuarioId { get; set; }

        public string Codigo { get; set; }

        public DateTime FechaExpiracion { get; set; }

        public CodigoActivacion(string usuarioId, TimeSpan tiempoValidez)
        {
            UsuarioId = usuarioId;
            Codigo = GenerarCodigo();
            FechaExpiracion = DateTime.UtcNow.Add(tiempoValidez);
        }

        private string GenerarCodigo()
        {
            return Guid.NewGuid().ToString().Replace("-", "").Substring(0, 8).ToUpper();
        }

        public bool EsValido()
        {
            return DateTime.UtcNow <= FechaExpiracion;
        }
    }
}
