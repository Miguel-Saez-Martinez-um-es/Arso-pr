using System;
using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;

namespace usuarios.Models
{
    public class Usuario
    {
        [BsonId]
        [BsonRepresentation(BsonType.String)] 
        public string Id { get; set; } = Guid.NewGuid().ToString(); 
        
        public string? Nombre { get; set; } 
        public string? Contrasena { get; set; } 
        public string? IdentificadorOAuth2 { get; set; } 

        public string NombreCompleto { get; set; }
        public string CorreoElectronico { get; set; }
        public string? Telefono { get; set; }
        public string? DireccionPostal { get; set; }

        [BsonRepresentation(BsonType.String)]
        public Rol Rol { get; set; } 

        public DateTime FechaRegistro { get; set; }

        public Usuario()
        {
            FechaRegistro = DateTime.UtcNow;
        }
    }

    public enum Rol
    {
        USER,
        ADMIN
    }
}
